package com.dzaitsev.marshmallows.dao.repository;

import com.dzaitsev.marshmallows.dao.entity.OrderLineEntity;
import com.dzaitsev.marshmallows.dao.entity.PriceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineRepository extends CrudRepository<OrderLineEntity, Integer> {

    @Query(value = "select ol.realPrice " +
            "from OrderLineEntity as ol where ol.realPrice.id = :priceId ")
    PriceEntity getIvolvedPrice(Integer priceId);
}
