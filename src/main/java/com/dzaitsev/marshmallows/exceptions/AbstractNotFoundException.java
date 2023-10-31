package com.dzaitsev.marshmallows.exceptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AbstractNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
