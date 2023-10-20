package com.dzaitsev.marshmallows.dao.repository.custom;

import com.dzaitsev.marshmallows.dao.entity.OrderEntity;
import com.dzaitsev.marshmallows.dto.OrderStatus;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepositoryCustom {

    List<OrderEntity> findByCriteria(LocalDate start, LocalDate end, List<OrderStatus> statuses);
}
