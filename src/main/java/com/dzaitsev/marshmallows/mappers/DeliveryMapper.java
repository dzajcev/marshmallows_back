package com.dzaitsev.marshmallows.mappers;

import com.dzaitsev.marshmallows.dao.entity.DeliveryEntity;
import com.dzaitsev.marshmallows.dao.entity.PriceEntity;
import com.dzaitsev.marshmallows.dto.Delivery;
import com.dzaitsev.marshmallows.dto.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryMapper extends Mapper<Delivery, DeliveryEntity> {

    private final OrderMapper orderMapper;
    @Override
    public Delivery toDto(DeliveryEntity delivery) {
        return Delivery.builder()
                .deliveryDate(delivery.getDeliveryDate())
                .createDate(delivery.getCreateDate())
                .id(delivery.getId())
                .end(delivery.getEnd())
                .start(delivery.getStart())
                .orders(orderMapper.toDto(delivery.getOrders()))
                .build();
    }
}
