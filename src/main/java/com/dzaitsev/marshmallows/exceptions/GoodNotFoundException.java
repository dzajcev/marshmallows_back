package com.dzaitsev.marshmallows.exceptions;

public class GoodNotFoundException extends RuntimeException{
    public GoodNotFoundException(String message) {
        super(message);
    }
}
