package com.dzaitsev.marshmallows.dto;

public enum OrderStatus {
    IN_PROGRESS("В процессе"),
    DONE("Выполнен"),
    IN_DELIVERY("В доставке"),
    SHIPPED("Доставлен");

    private final String text;

    OrderStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
