package com.dzaitsev.marshmallows.exceptions;

import lombok.Getter;

@Getter
public class AuthorizationException extends RuntimeException {

    private final ErrorCodes errorCodes;

    public AuthorizationException( ErrorCodes errorCodes,String message) {
        super(message);
        this.errorCodes = errorCodes;
    }

    public AuthorizationException(ErrorCodes errorCodes) {
        this.errorCodes = errorCodes;
    }
}
