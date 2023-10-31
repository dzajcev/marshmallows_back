package com.dzaitsev.marshmallows.service.impl;

import com.dzaitsev.marshmallows.dao.entity.UserEntity;
import com.dzaitsev.marshmallows.dao.repository.UserRepository;
import com.dzaitsev.marshmallows.dto.User;
import com.dzaitsev.marshmallows.dto.auth.SaveMyInfoRequest;
import com.dzaitsev.marshmallows.exceptions.AuthorizationException;
import com.dzaitsev.marshmallows.exceptions.ErrorCode;
import com.dzaitsev.marshmallows.mappers.UserMapper;
import com.dzaitsev.marshmallows.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = UserEntity.builder()
                .id(user.getId())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .createDate(user.getCreateDate())
                .email(user.getEmail())
                .password(user.getPassword())
                .verified(user.isEnabled())
                .role(user.getRole())
                .build();
        return userMapper.toDto(userRepository.save(userEntity));
    }

    @Override
    public void saveMyInfo(SaveMyInfoRequest request) {
        User userFromContext = getUserFromContext();
        userRepository.findByEmail(userFromContext.getEmail())
                .ifPresent(userEntity -> {
                    userEntity.setFirstName(request.getFirstName());
                    userEntity.setLastName(request.getLastName());
                });
    }


    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDto);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    public User getUserFromContext() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(m -> (User) m.getPrincipal())
                .orElseThrow(() -> new AuthorizationException(ErrorCode.AUTH004, ErrorCode.AUTH004.getText()));
    }
}