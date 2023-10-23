package com.dzaitsev.marshmallows.mappers;

import com.dzaitsev.marshmallows.dao.entity.GoodEntity;
import com.dzaitsev.marshmallows.dao.entity.PriceEntity;
import com.dzaitsev.marshmallows.dto.Good;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
@RequiredArgsConstructor
public class GoodMapper extends Mapper<Good, GoodEntity> {
    private final PriceMapper priceMapper;

    @Override
    public Good toDto(GoodEntity goodEntity) {
        if (goodEntity == null) {
            return null;
        }
        return Good.builder()
                .id(goodEntity.getId())
                .description(goodEntity.getDescription())
                .name(goodEntity.getName())
                .active(goodEntity.isActive())
                .price(goodEntity.getPrices().stream()
                        .max(Comparator.comparing(PriceEntity::getCreateDate))
                        .map(PriceEntity::getPrice)
                        .orElse(null))
                .prices(goodEntity.getPrices().stream()
                        .map(priceMapper::toDto).toList())
                .build();
    }
}