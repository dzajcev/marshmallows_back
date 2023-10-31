package com.dzaitsev.marshmallows.exceptions;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class AuthorizationException extends AuthenticationException {

    private final ErrorCode errorCode;

    public AuthorizationException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
