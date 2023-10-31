package com.dzaitsev.marshmallows.service.impl;

import com.dzaitsev.marshmallows.dao.entity.AbstractEntity;
import com.dzaitsev.marshmallows.dao.entity.AddInviteRequestsEntity;
import com.dzaitsev.marshmallows.dao.repository.InviteRequestsRepository;
import com.dzaitsev.marshmallows.dao.repository.UserRepository;
import com.dzaitsev.marshmallows.dto.InviteRequest;
import com.dzaitsev.marshmallows.dto.InviterRequestDirection;
import com.dzaitsev.marshmallows.dto.User;
import com.dzaitsev.marshmallows.exceptions.DeliveryExecutorNotFoundException;
import com.dzaitsev.marshmallows.exceptions.InviteRequestNotFoundException;
import com.dzaitsev.marshmallows.mappers.UserMapper;
import com.dzaitsev.marshmallows.service.InviteRequestService;
import com.dzaitsev.marshmallows.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class InviteRequestServiceImpl implements InviteRequestService {

    private final InviteRequestsRepository inviteRequestsRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final UserMapper userMapper;

    @Override
    public Collection<User> getDeliverymen() {
        Set<User> collect = Optional.of(userService.getUserFromContext())
                .map(User::getId)
                .map(id -> inviteRequestsRepository.findAllByUserCreate(id).stream())
                .orElse(Stream.of())
                .filter(f -> f.getAcceptedDate() != null)
                .map(addInviteRequestsEntity -> userService.findById(addInviteRequestsEntity.getDeliverymanUserId()).orElseThrow())
                .collect(Collectors.toSet());
        collect.add(userService.getUserFromContext());
        return collect;
    }

    @Override
    public void addInviteRequest(String login) {
        userRepository.findByEmail(login)
                .ifPresentOrElse(userEntity -> inviteRequestsRepository.save(AddInviteRequestsEntity.builder()
                        .deliverymanUserId(userEntity.getId())
                        .build()), () -> {
                    throw new DeliveryExecutorNotFoundException();
                });
    }

    @Override
    public void acceptInviteRequest(Integer requestId) {
        AddInviteRequestsEntity addInviteRequestsEntity = inviteRequestsRepository.findById(requestId)
                .filter(f -> f.getDeliverymanUserId().equals(userService.getUserFromContext().getId()))
                .orElseThrow(InviteRequestNotFoundException::new);
        addInviteRequestsEntity.setAcceptedDate(LocalDateTime.now());
    }

    @Override
    public List<InviteRequest> getInviteRequests(InviterRequestDirection direction, Boolean accepted) {
        List<AddInviteRequestsEntity> allRequests;
        switch (direction) {
            case OUTGOING ->
                    allRequests = inviteRequestsRepository.findAllByUserCreate(userService.getUserFromContext().getId());
            case INCOMING ->
                    allRequests = inviteRequestsRepository.findAllByDeliverymanUserId(userService.getUserFromContext().getId());
            default -> throw new RuntimeException("unknown invite request type");
        }
        if (Boolean.TRUE.equals(accepted)) {
            allRequests = allRequests.stream().filter(f -> f.getAcceptedDate() != null).toList();
        } else if (Boolean.FALSE.equals(accepted)) {
            allRequests = allRequests.stream().filter(f -> f.getAcceptedDate() == null).toList();
        }

        return mapInviteRequests(allRequests, direction);

    }

    @Override
    public void deleteInviteRequest(Integer requestId) {
        inviteRequestsRepository.deleteById(requestId);
    }

    private List<InviteRequest> mapInviteRequests(List<AddInviteRequestsEntity> requests, InviterRequestDirection inviterRequestDirection) {
        Set<Integer> userIds = requests.stream()
                .map(AddInviteRequestsEntity::getDeliverymanUserId).collect(Collectors.toSet());
        userIds.addAll(requests.stream().map(AbstractEntity::getUserCreate).collect(Collectors.toSet()));
        Map<Integer, User> map = StreamSupport.stream(userRepository.findAllById(userIds).spliterator(), false)
                .map(userMapper::toDto)
                .collect(Collectors.toMap(User::getId, y -> y));
        return requests.stream()
                .map(m -> InviteRequest.builder()
                        .id(m.getId())
                        .user(map.get(inviterRequestDirection == InviterRequestDirection.OUTGOING ? m.getDeliverymanUserId() : m.getUserCreate()))
                        .createDate(m.getCreateDate())
                        .acceptDate(m.getAcceptedDate())
                        .build()).toList();
    }
}
