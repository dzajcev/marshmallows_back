package com.dzaitsev.marshmallows.dao.repository;

import com.dzaitsev.marshmallows.dao.entity.ClientEntity;
import com.dzaitsev.marshmallows.dao.entity.GoodEntity;
import com.dzaitsev.marshmallows.dao.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface GoodsRepository extends CrudRepository<GoodEntity, Integer> {

    Iterable<GoodEntity> findGoodsEntitiesByActiveAndUserCreate(boolean isActive, Integer userCreate);

    Iterable<GoodEntity> findAllByUserCreate(Integer userCreate);

    Iterable<GoodEntity> findAllByIdInAndUserCreate (List<Integer>ids, Integer userCreate);

    Optional<GoodEntity> findByIdAndUserCreate(Integer id, Integer userCreate);
}
