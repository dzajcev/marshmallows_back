package com.dzaitsev.marshmallows.exceptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AbstractBadRequestException extends RuntimeException {

    private final ErrorCode errorCode;

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
