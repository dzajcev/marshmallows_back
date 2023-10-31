package com.dzaitsev.marshmallows.exceptions;

public class DeliveryExecutorNotFoundException extends AbstractNotFoundException{

    public DeliveryExecutorNotFoundException() {
        super(ErrorCode.IU001);
    }
}
