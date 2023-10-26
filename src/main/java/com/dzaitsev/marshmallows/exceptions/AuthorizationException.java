package com.dzaitsev.marshmallows.exceptions;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class AuthorizationException extends AuthenticationException {

    private final ErrorCodes errorCodes;

    public AuthorizationException( ErrorCodes errorCodes,String message) {
        super(message);
        this.errorCodes = errorCodes;
    }
}
