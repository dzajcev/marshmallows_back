package com.dzaitsev.marshmallows.exceptions;

public class DeleteDeliveryNotAllowException extends RuntimeException{
    public DeleteDeliveryNotAllowException(String message) {
        super(message);
    }
}
