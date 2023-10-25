package com.dzaitsev.marshmallows.service.impl;

import com.dzaitsev.marshmallows.dao.entity.UserEntity;
import com.dzaitsev.marshmallows.dao.repository.UserRepository;
import com.dzaitsev.marshmallows.dto.User;
import com.dzaitsev.marshmallows.mappers.UserMapper;
import com.dzaitsev.marshmallows.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

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
                .roles(user.getRoles().stream().map(Enum::name).collect(Collectors.joining(";")))
                .build();
        return userMapper.toDto(userRepository.save(userEntity));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDto);
    }


}