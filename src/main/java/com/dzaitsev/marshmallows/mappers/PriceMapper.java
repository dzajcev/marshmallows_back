package com.dzaitsev.marshmallows.mappers;

import com.dzaitsev.marshmallows.dao.entity.PriceEntity;
import com.dzaitsev.marshmallows.dto.Price;
import org.springframework.stereotype.Component;

@Component
public class PriceMapper extends Mapper<Price, PriceEntity> {

    @Override
    public Price toDto(PriceEntity priceEntity) {
        if (priceEntity == null) {
            return null;
        }
        return Price.builder()
                .id(priceEntity.getId())
                .createDate(priceEntity.getCreateDate())
                .price(priceEntity.getPrice())
                .build();
    }
}
