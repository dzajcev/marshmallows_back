package com.dzaitsev.marshmallows.schedule;

import com.dzaitsev.marshmallows.dao.entity.UserEntity;
import com.dzaitsev.marshmallows.dao.repository.JwtTokenRepository;
import com.dzaitsev.marshmallows.dao.repository.UserRepository;
import com.dzaitsev.marshmallows.dao.repository.VerificationCodeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ClearCodeAndUsersService {
    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;

    private final JwtTokenRepository jwtTokenRepository;

    @Transactional
    @Scheduled(fixedDelay = 1000 * 60)
    public void run() {
        List<Integer> ids = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .filter(f -> !f.isVerified())
                .filter(f -> f.getCreateDate().isBefore(LocalDateTime.now().minusHours(1)))
                .map(UserEntity::getId)
                .toList();
        verificationCodeRepository.deleteAllByUserIdIn(ids);
        userRepository.deleteAllById(ids);
    }

    @Transactional
    @Scheduled(initialDelay = 5000, fixedDelay = 1000 * 60)
    public void deleteTokens() {
        jwtTokenRepository.deleteAllByExpireDateBefore(LocalDateTime.now());
    }
}
