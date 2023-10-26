package com.dzaitsev.marshmallows.service;

import com.dzaitsev.marshmallows.dto.User;
import com.dzaitsev.marshmallows.dto.auth.ChangePasswordRequest;
import com.dzaitsev.marshmallows.dto.auth.JwtAuthenticationResponse;
import com.dzaitsev.marshmallows.dto.auth.VerificationCodeRequest;

public interface VerificationService {
    void sendVerificationCode();

    void sendVerificationCode(User user);
    JwtAuthenticationResponse verifyCode(VerificationCodeRequest request);

    void changePassword(ChangePasswordRequest request);
}
