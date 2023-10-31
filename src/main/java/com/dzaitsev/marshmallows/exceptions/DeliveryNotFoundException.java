package com.dzaitsev.marshmallows.exceptions;

public class DeliveryNotFoundException extends AbstractNotFoundException{

    public DeliveryNotFoundException() {
        super(ErrorCode.DEL001);
    }
}
