package com.dzaitsev.marshmallows.dao.repository;

import com.dzaitsev.marshmallows.dao.entity.DeliveryEntity;
import com.dzaitsev.marshmallows.dto.DeliveryStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface DeliveryRepository extends CrudRepository<DeliveryEntity, Integer> {
    List<DeliveryEntity> findOrderEntitiesByCreateDateAfterAndCreateDateBeforeAndDeliveryStatusInAndUserCreate(LocalDateTime start, LocalDateTime end, List<DeliveryStatus> statuses, Integer userCreate);

    Optional<DeliveryEntity> findByIdAndUserCreate(Integer id, Integer userCreate);

}
