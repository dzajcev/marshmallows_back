package com.dzaitsev.marshmallows.service.impl;

import com.dzaitsev.marshmallows.dao.entity.GoodEntity;
import com.dzaitsev.marshmallows.dao.entity.PriceEntity;
import com.dzaitsev.marshmallows.dao.repository.GoodsRepository;
import com.dzaitsev.marshmallows.dao.repository.OrderLineRepository;
import com.dzaitsev.marshmallows.dto.Good;
import com.dzaitsev.marshmallows.exceptions.GoodNotFoundException;
import com.dzaitsev.marshmallows.mappers.GoodMapper;
import com.dzaitsev.marshmallows.service.GoodsService;
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
@Transactional
public class GoodsServiceImpl extends AbstractService implements GoodsService {

    private final GoodsRepository goodsRepository;

    private final OrderLineRepository orderLineRepository;

    private final GoodMapper goodMapper;

    @Override
    public List<Good> getGoods(Boolean isActive) {
        Iterable<GoodEntity> result;
        if (Boolean.TRUE.equals(isActive)) {
            result = goodsRepository.findGoodsEntitiesByActiveAndUserCreate(true, getUserFromContext().getId());
        } else if (Boolean.FALSE.equals(isActive)) {
            result = goodsRepository.findGoodsEntitiesByActiveAndUserCreate(false, getUserFromContext().getId());
        } else {
            result = goodsRepository.findAllByUserCreate(getUserFromContext().getId());
        }
        return StreamSupport.stream(result.spliterator(), false)
                .map(goodMapper::toDto)
                .toList();
    }

    @Override
    public Good getGood(Integer id) {
        return goodsRepository.findByIdAndUserCreate(id, getUserFromContext().getId())
                .map(goodMapper::toDto)
                .orElseThrow(() -> new GoodNotFoundException(String.format("good with id %s not found", id)));
    }

    @Override
    public void saveGood(Good good) {
        GoodEntity goodEntity = Optional.ofNullable(good.getId())
                .flatMap(m -> goodsRepository.findByIdAndUserCreate(good.getId(), getUserFromContext().getId()))
                .orElse(GoodEntity.builder()
                        .build());
        goodEntity.setName(good.getName());
        goodEntity.setActive(good.isActive());
        goodEntity.setDescription(good.getDescription());
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

    @Override
    public void deleteGood(Integer id) {
        goodsRepository.findByIdAndUserCreate(id, getUserFromContext().getId())
                .ifPresent(goodEntity -> {
                    if (!goodEntity.getOrderLines().isEmpty()) {
                        goodEntity.setActive(false);
                    } else {
                        goodsRepository.delete(goodEntity);
                    }
                });
    }

    @Override
    public void restoreGood(Integer id) {
        goodsRepository.findByIdAndUserCreate(id, getUserFromContext().getId())
                .ifPresent(clientEntity -> clientEntity.setActive(true));
    }

    @Override
    public boolean goodWithOrderLines(Integer id) {
        return goodsRepository.findByIdAndUserCreate(id, getUserFromContext().getId())
                .map(m -> !m.getOrderLines().isEmpty()).orElse(false);
    }
}
