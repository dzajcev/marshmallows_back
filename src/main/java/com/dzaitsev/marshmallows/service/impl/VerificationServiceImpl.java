package com.dzaitsev.marshmallows.service.impl;

import com.dzaitsev.marshmallows.dao.entity.VerificationCodeEntity;
import com.dzaitsev.marshmallows.dao.repository.JwtTokenRepository;
import com.dzaitsev.marshmallows.dao.repository.UserRepository;
import com.dzaitsev.marshmallows.dao.repository.VerificationCodeRepository;
import com.dzaitsev.marshmallows.dto.User;
import com.dzaitsev.marshmallows.dto.auth.ChangePasswordRequest;
import com.dzaitsev.marshmallows.dto.auth.JwtAuthenticationResponse;
import com.dzaitsev.marshmallows.dto.auth.VerificationCodeRequest;
import com.dzaitsev.marshmallows.exceptions.AuthorizationException;
import com.dzaitsev.marshmallows.exceptions.ErrorCodes;
import com.dzaitsev.marshmallows.service.EmailService;
import com.dzaitsev.marshmallows.service.JwtService;
import com.dzaitsev.marshmallows.service.UserService;
import com.dzaitsev.marshmallows.service.VerificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@Slf4j
public class VerificationServiceImpl implements VerificationService {

    private final PasswordEncoder passwordEncoder;
    @Value("${authorization.verification-code-ttl}")
    private Integer verificationCodeTtl;
    private final VerificationCodeRepository verificationCodeRepository;

    private final UserRepository userRepository;


    private final JwtTokenRepository jwtTokenRepository;

    private final EmailService emailService;

    private final UserService userService;

    private final JwtService jwtService;
    private final ExecutorService executor = Executors.newFixedThreadPool(3);

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

    @Override
    public void sendVerificationCode(User user) {
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
    @Transactional
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

    public User getUserFromContext() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(m -> (User) m.getPrincipal())
                .orElseThrow(() -> new AuthorizationException(ErrorCodes.AUTH004, ErrorCodes.AUTH004.getText()));
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {

        User userFromContext = getUserFromContext();
        jwtTokenRepository.deleteAllByUserName(userFromContext.getEmail());
        userRepository.findByEmail(userFromContext.getEmail())
                .ifPresent(userEntity -> userEntity.setPassword(passwordEncoder.encode(request.getPassword())));
    }
}
