package com.dzaitsev.marshmallows.mappers;

import com.dzaitsev.marshmallows.dao.entity.GoodEntity;
import com.dzaitsev.marshmallows.dao.entity.OrderLineEntity;
import com.dzaitsev.marshmallows.dao.entity.PriceEntity;
import com.dzaitsev.marshmallows.dto.Good;
import com.dzaitsev.marshmallows.dto.OrderLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
@RequiredArgsConstructor
public class OrderLineMapper extends Mapper<OrderLine, OrderLineEntity> {

    private final GoodMapper goodMapper;
    @Override
    public OrderLine toDto(OrderLineEntity orderLineEntity) {
        return OrderLine.builder()
                .id(orderLineEntity.getId())
                .createDate(orderLineEntity.getCreateDate())
                .done(orderLineEntity.getDone())
                .good(goodMapper.toDto(orderLineEntity.getGood()))
                .num(orderLineEntity.getNum())
                .count(orderLineEntity.getCount())
                .price(orderLineEntity.getPrice())
                .build();
    }
}