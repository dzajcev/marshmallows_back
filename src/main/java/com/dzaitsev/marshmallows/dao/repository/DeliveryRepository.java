package com.dzaitsev.marshmallows.dao.repository;

import com.dzaitsev.marshmallows.dao.entity.DeliveryEntity;
import com.dzaitsev.marshmallows.dao.entity.OrderEntity;
import com.dzaitsev.marshmallows.dao.repository.custom.DeliveryRepositoryCustom;
import com.dzaitsev.marshmallows.dao.repository.custom.OrderRepositoryCustom;
import com.dzaitsev.marshmallows.dto.DeliveryStatus;
import com.dzaitsev.marshmallows.dto.OrderStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeliveryRepository extends CrudRepository<DeliveryEntity, Integer>, DeliveryRepositoryCustom {
    List<DeliveryEntity> findOrderEntitiesByCreateDateAfterAndCreateDateBeforeAndDeliveryStatusIn(LocalDateTime start, LocalDateTime end, List<DeliveryStatus> statuses);
}
