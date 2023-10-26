package com.dzaitsev.marshmallows.config;

import com.dzaitsev.marshmallows.dao.repository.JwtTokenRepository;
import com.dzaitsev.marshmallows.exceptions.TokenExpiredException;
import com.dzaitsev.marshmallows.service.JwtService;
import com.dzaitsev.marshmallows.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationEntryPoint entryPoint;

    private final JwtTokenRepository jwtTokenRepository;

    private final Set<String> verificationUris = Stream.of("/auth/verify-code", "/auth/send-code").collect(Collectors.toSet());

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")
                || request.getRequestURI().equals("/auth/signin")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        try {
            JwtService.TokenType tokenType = jwtService.extractTokenType(jwt);
            if ((tokenType == JwtService.TokenType.SIGN_UP
                    && !verificationUris.contains(request.getRequestURI())
                    || (tokenType == JwtService.TokenType.SIGN_IN
                    && verificationUris.contains(request.getRequestURI())))
                    || !jwtTokenRepository.existsById(jwt)) {
                throw new IllegalArgumentException("Token is invalid");
            }
            userEmail = jwtService.extractUserName(jwt);
            if (StringUtils.isNotEmpty(userEmail)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                userService.findByEmail(userEmail).ifPresent(user -> {
                    boolean toCheckEnabled = tokenType == JwtService.TokenType.SIGN_IN;
                    if (toCheckEnabled  && !user.isEnabled()) {
                        throw new DisabledException("user not active");
                    }
                    if ((!toCheckEnabled || user.isEnabled()) && jwtService.isTokenValid(jwt, user)) {
                        SecurityContext context = SecurityContextHolder.createEmptyContext();
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                user, null, user.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        context.setAuthentication(authToken);
                        SecurityContextHolder.setContext(context);
                    }
                });
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException expiredJwtException) {
            TokenExpiredException tokenExpiredException = new TokenExpiredException(expiredJwtException.getMessage(), expiredJwtException);
            entryPoint.commence(request, response, tokenExpiredException);
        } catch (DisabledException e) {
            entryPoint.commence(request, response, e);
        }

    }
}