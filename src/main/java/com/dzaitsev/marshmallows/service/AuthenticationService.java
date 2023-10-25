package com.dzaitsev.marshmallows.service;

import com.dzaitsev.marshmallows.dto.auth.*;

public interface AuthenticationService {
    JwtSignUpResponse signUp(SignUpRequest request);

    void sendVerificationCode();

    JwtAuthenticationResponse signIn(SignInRequest request);

    JwtAuthenticationResponse verifyCode(VerificationCodeRequest request);
}