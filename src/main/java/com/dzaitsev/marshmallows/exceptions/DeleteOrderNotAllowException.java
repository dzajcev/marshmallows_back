package com.dzaitsev.marshmallows.exceptions;

public class DeleteOrderNotAllowException extends RuntimeException{
    public DeleteOrderNotAllowException(String message) {
        super(message);
    }
}
