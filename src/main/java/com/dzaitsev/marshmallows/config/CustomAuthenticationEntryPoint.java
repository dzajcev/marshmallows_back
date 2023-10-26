package com.dzaitsev.marshmallows.config;

import com.dzaitsev.marshmallows.dto.ErrorDto;
import com.dzaitsev.marshmallows.exceptions.ErrorCodes;
import com.dzaitsev.marshmallows.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(403);
        ErrorDto errorDto;
        if (authException instanceof TokenExpiredException) {
            errorDto = new ErrorDto(ErrorCodes.AUTH006);
        }else if (authException instanceof BadCredentialsException){
            errorDto = new ErrorDto(ErrorCodes.AUTH007);
        } else if (authException instanceof DisabledException){
            errorDto = new ErrorDto(ErrorCodes.AUTH008);
        }else {
            errorDto = new ErrorDto(ErrorCodes.AUTH000);
        }
        log.error("authentication error", authException);
        response.getWriter().write(objectMapper.writeValueAsString(errorDto));

    }
}
