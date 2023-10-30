package com.dzaitsev.marshmallows.dao.repository;

import com.dzaitsev.marshmallows.dao.entity.DeliveryEntity;
import com.dzaitsev.marshmallows.dto.DeliveryStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends CrudRepository<DeliveryEntity, Integer> {
    @Query("SELECT d FROM DeliveryEntity d WHERE d.createDate>=?1 AND d.createDate<=?2" +
            " AND d.deliveryStatus IN ?3 AND (d.userCreate=?4 OR d.executor.id=?4)")
    List<DeliveryEntity> getAvailableDeliveries(LocalDateTime start, LocalDateTime end, List<DeliveryStatus> statuses, Integer userCreate);

    @Query("SELECT d FROM DeliveryEntity d WHERE d.id=?1 AND (d.userCreate=?2 OR d.executor.id=?2)")
    Optional<DeliveryEntity> findById(Integer id, Integer userCreate);

}
