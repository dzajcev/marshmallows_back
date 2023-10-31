package com.dzaitsev.marshmallows.dao.repository;

import com.dzaitsev.marshmallows.dao.entity.AddInviteRequestsEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InviteRequestsRepository extends CrudRepository<AddInviteRequestsEntity, Integer> {
    List<AddInviteRequestsEntity> findAllByUserCreate(Integer userId);

    List<AddInviteRequestsEntity> findAllByDeliverymanUserId(Integer userId);
}
