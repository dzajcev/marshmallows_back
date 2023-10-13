package com.dzaitsev.marshmallows.mappers;

import com.dzaitsev.marshmallows.dao.entity.GoodEntity;
import com.dzaitsev.marshmallows.dao.entity.PriceEntity;
import com.dzaitsev.marshmallows.dto.Good;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class GoodMapper extends Mapper<Good, GoodEntity> {
    @Override
    public Good toDto(GoodEntity goodEntity) {
        return Good.builder()
                .id(goodEntity.getId())
                .description(goodEntity.getDescription())
                .name(goodEntity.getName())
                .price(goodEntity.getPrices().stream()
                        .max(Comparator.comparing(PriceEntity::getCreateDate))
                        .map(PriceEntity::getPrice)
                        .orElse(null))
                .build();
    }
}