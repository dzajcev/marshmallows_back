package com.dzaitsev.marshmallows.mappers;

import com.dzaitsev.marshmallows.dao.entity.OrderLineEntity;
import com.dzaitsev.marshmallows.dto.OrderLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderLineMapper extends Mapper<OrderLine, OrderLineEntity> {

    private final GoodMapper goodMapper;

    @Override
    public OrderLine toDto(OrderLineEntity orderLineEntity) {
        if (orderLineEntity == null) {
            return null;
        }
        return OrderLine.builder()
                .id(orderLineEntity.getId())
                .createDate(orderLineEntity.getCreateDate())
                .done(orderLineEntity.isDone())
                .good(goodMapper.toDto(orderLineEntity.getGood()))
                .num(orderLineEntity.getNum())
                .count(orderLineEntity.getCount())
                .price(orderLineEntity.getPrice())
                .build();
    }
}