package com.dzaitsev.marshmallows.service.impl;

import com.dzaitsev.marshmallows.dao.entity.VerificationCodeEntity;
import com.dzaitsev.marshmallows.dao.repository.VerificationCodeRepository;
import com.dzaitsev.marshmallows.dto.User;
import com.dzaitsev.marshmallows.dto.auth.*;
import com.dzaitsev.marshmallows.exceptions.AuthorizationException;
import com.dzaitsev.marshmallows.exceptions.ErrorCodes;
import com.dzaitsev.marshmallows.service.AuthenticationService;
import com.dzaitsev.marshmallows.service.EmailService;
import com.dzaitsev.marshmallows.service.JwtService;
import com.dzaitsev.marshmallows.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${authorization.verification-code-ttl}")
    private Integer verificationCodeTtl;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final VerificationCodeRepository verificationCodeRepository;

    private final EmailService emailService;

    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    @Override
    public JwtSignUpResponse signUp(SignUpRequest request) {
        return Optional.of(request)
                .map(m -> {
                    User user = userService.findByEmail(m.getEmail())
                            .orElse(null);
                    if (user != null) {
                        if (!user.isEnabled()) {
                            try {
                                user.setEnabled(true);
                                userService.save(user);
                                try {
                                    authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
                                } catch (Exception e) {
                                    log.warn("");
                                }
                                return user;
                            } finally {
                                user.setEnabled(false);
                                userService.save(user);
                            }

                        } else {
                            throw new AuthorizationException(ErrorCodes.AUTH003, ErrorCodes.AUTH003.getText());
                        }
                    }
                    User newUser = User.builder()
                            .lastName(m.getLastName())
                            .firstName(m.getFirstName())
                            .email(m.getEmail())
                            .password(passwordEncoder.encode(m.getPassword()))
                            .roles(m.getRoles())
                            .build();
                    User save = userService.save(newUser);
                    sendVerificationCode(save);
                    return save;
                })
                .map(m -> jwtService.generateToken(m, JwtService.TokenType.SIGN_UP))
                .map(m -> JwtSignUpResponse.builder()
                        .token(m)
                        .verificatonCodeTtl(verificationCodeTtl)
                        .build())
                .orElseThrow();
    }

    @Override
    public void sendVerificationCode() {
        if (getUserFromContext().isEnabled()) {
            return;
        }
        VerificationCodeEntity lastCode;
        try {
            lastCode = getLastCode();
        } catch (AuthorizationException e) {
            lastCode = null;
        }
        if (lastCode != null && LocalDateTime.now().minusSeconds(verificationCodeTtl).isBefore(lastCode.getCreateDate())) {
            throw new AuthorizationException(ErrorCodes.AUTH005, ErrorCodes.AUTH005.getText());
        } else {
            sendVerificationCode(getUserFromContext());
        }
    }

    private void sendVerificationCode(User user) {
        executor.submit(() -> {
            try {
                String code = Integer.toString(ThreadLocalRandom.current().nextInt(100000, 999999));
                VerificationCodeEntity verificationCodeEntity = VerificationCodeEntity.builder()
                        .code(code)
                        .userId(user.getId())
                        .ttl(LocalDateTime.now().plusMinutes(JwtService.TokenType.SIGN_UP.getTtl()))
                        .build();
                if (emailService.sendVerificationCode(user.getEmail(), code)) {
                    verificationCodeRepository.save(verificationCodeEntity);
                } else {
                    log.warn("verification code not sended");
                }
            } catch (Exception e) {
                log.error("error of sending verification code", e);
            }
        });
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        return userService.findByEmail(request.getEmail())
                .map(m -> jwtService.generateToken(m, JwtService.TokenType.SIGN_IN))
                .map(m -> JwtAuthenticationResponse.builder().token(m).build())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    }

    private VerificationCodeEntity getLastCode() {
        User user = getUserFromContext();
        LinkedList<VerificationCodeEntity> verificationCodes = new LinkedList<>(verificationCodeRepository.getValidatingCodesByUserIdOrderByCreateDate(user.getId()));
        if (verificationCodes.isEmpty()) {
            log.warn("codes for user {} not exists", user.getEmail());
            throw new AuthorizationException(ErrorCodes.AUTH002, String.format("Код для пользователя %s просрочен. Запросите новый", user.getEmail()));
        }
        return verificationCodes.getLast();

    }

    @Override
    public JwtAuthenticationResponse verifyCode(VerificationCodeRequest request) {
        User user = getUserFromContext();
        if (!user.isEnabled()) {
            VerificationCodeEntity last = getLastCode();

            if (last.getTtl().isBefore(LocalDateTime.now())) {
                log.warn("codes for user {} is expired", user.getEmail());
                verificationCodeRepository.deleteAllByUserId(user.getId());
                throw new AuthorizationException(ErrorCodes.AUTH002, String.format("Код для пользователя %s просрочен. Запросите новый", user.getEmail()));
            }
            if (!request.getCode().equals(last.getCode())) {
                throw new AuthorizationException(ErrorCodes.AUTH001, String.format("Код для пользователя %s не корректный", user.getEmail()));
            }
            user.setEnabled(true);
            userService.save(user);
            verificationCodeRepository.deleteAllByUserId(user.getId());
        }
        return Optional.of(user)
                .map(m -> jwtService.generateToken(m, JwtService.TokenType.SIGN_IN))
                .map(JwtAuthenticationResponse::new)
                .orElseThrow(() -> new RuntimeException("validate code failed"));
    }

    private User getUserFromContext() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(m -> (User) m.getPrincipal())
                .orElseThrow(() -> new AuthorizationException(ErrorCodes.AUTH004, ErrorCodes.AUTH004.getText()));
    }
}