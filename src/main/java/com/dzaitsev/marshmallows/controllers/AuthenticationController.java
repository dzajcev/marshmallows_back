package com.dzaitsev.marshmallows.controllers;

import com.dzaitsev.marshmallows.dto.auth.*;
import com.dzaitsev.marshmallows.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;


    @PostMapping("/signup")
    public ResponseEntity<JwtSignUpResponse> signup(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signUp(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signIn(request));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<JwtAuthenticationResponse> verifyCode(@RequestBody VerificationCodeRequest request) {
        return ResponseEntity.ok(authenticationService.verifyCode(request));
    }

    @PostMapping("/send-code")
    public ResponseEntity<Void> sendCode() {
        authenticationService.sendVerificationCode();
        return ResponseEntity.noContent().build();
    }
}