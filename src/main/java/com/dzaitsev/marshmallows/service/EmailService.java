package com.dzaitsev.marshmallows.service;

public interface EmailService {

    boolean sendVerificationCode(String recipient, String code);
}
