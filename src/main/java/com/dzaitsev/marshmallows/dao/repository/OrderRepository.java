package com.dzaitsev.marshmallows.dao.repository;

import com.dzaitsev.marshmallows.dao.entity.DeliveryEntity;
import com.dzaitsev.marshmallows.dao.entity.OrderEntity;
import com.dzaitsev.marshmallows.dao.repository.custom.OrderRepositoryCustom;
import com.dzaitsev.marshmallows.dto.OrderStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Integer>, OrderRepositoryCustom {

    @Query(value = "SELECT o FROM OrderEntity o LEFT JOIN o.delivery d " +
            "WHERE o.needDelivery AND NOT o.shipped " +
            "AND d IS NULL AND NOT EXISTS (SELECT ol FROM OrderLineEntity ol WHERE ol.order=o AND NOT ol.done) AND o.userCreate=?1")
    List<OrderEntity> getOrdersForDelivery(Integer userCreate);

    List<OrderEntity> findOrderEntitiesByCreateDateAfterAndCreateDateBeforeAndOrderStatusInAndUserCreate(LocalDateTime start, LocalDateTime end, List<OrderStatus> statuses,
                                                                                                         Integer userCreate);

    Iterable<OrderEntity> findAllByUserCreate(Integer userCreate);
    Iterable<OrderEntity> findAllByIdInAndUserCreate (List<Integer>ids, Integer userCreate);
    Optional<OrderEntity> findByIdAndUserCreate(Integer id, Integer userCreate);
}

