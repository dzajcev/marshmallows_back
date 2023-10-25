package com.dzaitsev.marshmallows.exceptions;

import org.springframework.security.core.AuthenticationException;

public class TokenExpiredException extends AuthenticationException {
    public TokenExpiredException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
