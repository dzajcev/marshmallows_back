package com.dzaitsev.marshmallows.service.impl;

import com.dzaitsev.marshmallows.dao.entity.AddDeliverymanRequestsEntity;
import com.dzaitsev.marshmallows.dao.entity.UserEntity;
import com.dzaitsev.marshmallows.dao.repository.AddDeliverymanRequestsRepository;
import com.dzaitsev.marshmallows.dao.repository.UserRepository;
import com.dzaitsev.marshmallows.dto.User;
import com.dzaitsev.marshmallows.dto.UserRole;
import com.dzaitsev.marshmallows.dto.auth.SaveMyInfoRequest;
import com.dzaitsev.marshmallows.exceptions.AuthorizationException;
import com.dzaitsev.marshmallows.exceptions.ErrorCodes;
import com.dzaitsev.marshmallows.exceptions.InviteRequestNotFoundException;
import com.dzaitsev.marshmallows.mappers.UserMapper;
import com.dzaitsev.marshmallows.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final AddDeliverymanRequestsRepository addDeliverymanRequestsRepository;

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
    public Collection<User> getAssociatedUser(UserRole role) {
        return userRepository.findById(getUserFromContext().getId())
                .map((Function<UserEntity, Collection<UserEntity>>) userEntity -> {
                    switch (role) {
                        case DELIVERYMAN -> {
                            return userEntity.getDeliverymans();
                        }
                        case DEVELOPER -> {
                            return userEntity.getDevelopers();
                        }
                        default -> throw new RuntimeException("unknown role");
                    }
                })
                .map(m -> m.stream()
                        .map(userMapper::toDto)
                        .collect(Collectors.toSet())
                )
                .orElse(new HashSet<>());
    }

    @Override
    public void addDeliveryman(Integer deliverymanId) {
        addDeliverymanRequestsRepository.save(AddDeliverymanRequestsEntity.builder()
                .deliverymanUserId(deliverymanId)
                .build());

    }

    @Override
    public void acceptInviteRequest(Integer requestId) {
        AddDeliverymanRequestsEntity addDeliverymanRequestsEntity = addDeliverymanRequestsRepository.findById(requestId)
                .filter(f -> f.getDeliverymanUserId().equals(getUserFromContext().getId()))
                .orElseThrow(() -> new InviteRequestNotFoundException(String.format("Request %s not found", requestId)));
        addDeliverymanRequestsEntity.setAcceptedDate(LocalDateTime.now());
        UserEntity me = userRepository.findById(getUserFromContext().getId()).orElseThrow();
        UserEntity inviting = userRepository.findById(addDeliverymanRequestsEntity.getUserCreate()).orElseThrow();
        me.getDevelopers().add(inviting);
        inviting.getDeliverymans().add(me);
    }

    private User getUserFromContext() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(m -> (User) m.getPrincipal())
                .orElseThrow(() -> new AuthorizationException(ErrorCodes.AUTH004, ErrorCodes.AUTH004.getText()));
    }
}