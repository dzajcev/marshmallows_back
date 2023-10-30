package com.dzaitsev.marshmallows.exceptions;

public class DeliveryExecutorNotFoundException extends RuntimeException{
    public DeliveryExecutorNotFoundException(String message) {
        super(message);
    }
}
