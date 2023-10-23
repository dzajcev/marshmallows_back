package com.dzaitsev.marshmallows.dao.repository;

import com.dzaitsev.marshmallows.dao.entity.OrderEntity;
import com.dzaitsev.marshmallows.dao.repository.custom.OrderRepositoryCustom;
import com.dzaitsev.marshmallows.dto.OrderStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Integer>, OrderRepositoryCustom {

    @Query(value = "SELECT o FROM OrderEntity o LEFT JOIN o.delivery d " +
            "WHERE o.needDelivery AND NOT o.shipped " +
            "AND d IS NULL AND NOT EXISTS (SELECT ol FROM OrderLineEntity ol WHERE ol.order=o AND NOT ol.done)")
    List<OrderEntity> getOrdersForDelivery();

    List<OrderEntity> findOrderEntitiesByCreateDateAfterAndCreateDateBeforeAndOrderStatusIn(LocalDateTime start, LocalDateTime end, List<OrderStatus> statuses);
}
