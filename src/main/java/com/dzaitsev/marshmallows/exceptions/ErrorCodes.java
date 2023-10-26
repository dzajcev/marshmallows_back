package com.dzaitsev.marshmallows.exceptions;

public enum ErrorCodes {
    AUTH000("Неизвестная ошибка"),
    AUTH001("Код верификации не верный"),
    AUTH002("Код верификации просрочен"),
    AUTH003("Пользователь с такой почтой зарегистрирован"),
    AUTH004("Пользователь не найден"),
    AUTH005("Еще рано отправлять новый код"),
    AUTH006("Срок жизни токена истек"),
    AUTH007("Неверный логин или пароль"),
    AUTH008("Требуется подтверждение учетной записи"),
    ;

    private final String text;

    ErrorCodes(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
