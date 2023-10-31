package com.dzaitsev.marshmallows.config;

import com.dzaitsev.marshmallows.dto.ErrorDto;
import com.dzaitsev.marshmallows.exceptions.ErrorCode;
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
            errorDto = new ErrorDto(ErrorCode.AUTH006, ErrorCode.AUTH006.getText());
        } else if (authException instanceof BadCredentialsException) {
            errorDto = new ErrorDto(ErrorCode.AUTH007, ErrorCode.AUTH007.getText());
        } else if (authException instanceof DisabledException) {
            errorDto = new ErrorDto(ErrorCode.AUTH008, ErrorCode.AUTH008.getText());
        } else {
            errorDto = new ErrorDto(ErrorCode.AUTH000, ErrorCode.AUTH000.getText());
        }
        log.error("authentication error", authException);
        response.getWriter().write(objectMapper.writeValueAsString(errorDto));

    }
}
