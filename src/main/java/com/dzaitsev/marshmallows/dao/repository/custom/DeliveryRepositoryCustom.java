package com.dzaitsev.marshmallows.dao.repository.custom;

import com.dzaitsev.marshmallows.dao.entity.DeliveryEntity;
import com.dzaitsev.marshmallows.dao.entity.OrderEntity;
import com.dzaitsev.marshmallows.dto.DeliveryStatus;
import com.dzaitsev.marshmallows.dto.OrderStatus;

import java.time.LocalDate;
import java.util.List;

public interface DeliveryRepositoryCustom {

    List<DeliveryEntity> findByCriteria(LocalDate start, LocalDate end, List<DeliveryStatus> statuses);
}
