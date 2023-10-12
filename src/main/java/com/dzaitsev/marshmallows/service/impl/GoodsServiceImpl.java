package com.dzaitsev.marshmallows.service.impl;

import com.dzaitsev.marshmallows.dto.Good;
import com.dzaitsev.marshmallows.service.GoodsService;
import com.dzaitsev.marshmallows.dao.entity.GoodEntity;
import com.dzaitsev.marshmallows.dao.entity.PriceEntity;
import com.dzaitsev.marshmallows.dao.repository.GoodsRepository;
import com.dzaitsev.marshmallows.dao.repository.OrderLineRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    private final GoodsRepository goodsRepository;

    private final OrderLineRepository orderLineRepository;

    @Override
    @Transactional
    public List<Good> getGoods() {
        return StreamSupport.stream(goodsRepository.findAll().spliterator(), false)
                .map(m -> Good.builder()
                        .id(m.getId())
                        .name(m.getName())
                        .price(m.getPrices().stream().max(Comparator.comparing(PriceEntity::getCreateDate))
                                .map(PriceEntity::getPrice)
                                .orElse(null))
                        .build()).toList();
    }

    @Override
    @Transactional
    public void saveGood(Good good) {
        GoodEntity goodEntity = Optional.ofNullable(good.getId())
                .flatMap(m -> goodsRepository.findById(good.getId()))
                .orElse(GoodEntity.builder()
                        .build());
        goodEntity.setName(good.getName());
        List<PriceEntity> prices = goodEntity.getPrices();
        if (prices == null) {
            goodEntity.setPrices(new ArrayList<>());
            goodEntity.getPrices().add(PriceEntity.builder()
                            .price(good.getPrice())
                            .good(goodEntity)
                    .build());
        } else {
            goodEntity.getPrices().stream().max(Comparator.comparing(PriceEntity::getCreateDate))
                    .ifPresent(priceEntity -> {
                        PriceEntity ivolvedPrice = orderLineRepository.getIvolvedPrice(priceEntity.getId());
                        if (ivolvedPrice != null) {
                            goodEntity.getPrices().add(PriceEntity.builder()
                                    .good(goodEntity)
                                    .price(good.getPrice())
                                    .build());
                        } else {
                            priceEntity.setPrice(good.getPrice());
                        }
                    });
        }

        goodsRepository.save(goodEntity);
    }
}