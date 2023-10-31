package com.dzaitsev.marshmallows.exceptions;

public class InviteRequestNotFoundException extends AbstractNotFoundException {

    public InviteRequestNotFoundException() {
        super(ErrorCode.IR001);
    }
}
