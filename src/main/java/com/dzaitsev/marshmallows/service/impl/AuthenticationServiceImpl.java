package com.dzaitsev.marshmallows.service.impl;

import com.dzaitsev.marshmallows.dto.User;
import com.dzaitsev.marshmallows.dto.auth.JwtAuthenticationResponse;
import com.dzaitsev.marshmallows.dto.auth.JwtSignUpResponse;
import com.dzaitsev.marshmallows.dto.auth.SignInRequest;
import com.dzaitsev.marshmallows.dto.auth.SignUpRequest;
import com.dzaitsev.marshmallows.exceptions.AuthorizationException;
import com.dzaitsev.marshmallows.exceptions.ErrorCodes;
import com.dzaitsev.marshmallows.service.AuthenticationService;
import com.dzaitsev.marshmallows.service.JwtService;
import com.dzaitsev.marshmallows.service.UserService;
import com.dzaitsev.marshmallows.service.VerificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    @Value("${authorization.verification-code-ttl}")
    private Integer verificationCodeTtl;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final VerificationService verificationService;


    @Override
    public JwtSignUpResponse signUp(SignUpRequest request) {
        return Optional.of(request)
                .map(m -> {
                    User user = userService.findByEmail(m.getEmail())
                            .orElse(null);
                    if (user != null) {
                        if (!user.isEnabled()) {
                            try {
                                user.setEnabled(true);
                                userService.save(user);
                                authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
                                verificationService.sendVerificationCode(user);
                                return user;
                            } finally {
                                user.setEnabled(false);
                                userService.save(user);
                            }
                        } else {
                            throw new AuthorizationException(ErrorCodes.AUTH003, ErrorCodes.AUTH003.getText());
                        }
                    }
                    User newUser = User.builder()
                            .lastName(m.getLastName())
                            .firstName(m.getFirstName())
                            .email(m.getEmail())
                            .password(passwordEncoder.encode(m.getPassword()))
                            .roles(m.getRoles())
                            .build();
                    User save = userService.save(newUser);
                    verificationService.sendVerificationCode(save);
                    return save;
                })
                .map(m -> jwtService.generateToken(m, JwtService.TokenType.SIGN_UP))
                .map(m -> JwtSignUpResponse.builder()
                        .token(m)
                        .verificationCodeTtl(verificationCodeTtl)
                        .build())
                .orElseThrow();
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        return userService.findByEmail(request.getEmail())
                .map(m -> jwtService.generateToken(m, JwtService.TokenType.SIGN_IN))
                .map(m -> JwtAuthenticationResponse.builder().token(m).build())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    }

    public User getUserFromContext() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(m -> (User) m.getPrincipal())
                .orElseThrow(() -> new AuthorizationException(ErrorCodes.AUTH004, ErrorCodes.AUTH004.getText()));
    }

}