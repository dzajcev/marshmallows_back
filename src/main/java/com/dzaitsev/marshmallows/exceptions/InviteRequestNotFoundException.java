package com.dzaitsev.marshmallows.exceptions;

public class InviteRequestNotFoundException extends RuntimeException{
    public InviteRequestNotFoundException() {
    }

    public InviteRequestNotFoundException(String message) {
        super(message);
    }
}
