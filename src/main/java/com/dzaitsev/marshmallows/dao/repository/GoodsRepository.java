package com.dzaitsev.marshmallows.dao.repository;

import com.dzaitsev.marshmallows.dao.entity.ClientEntity;
import com.dzaitsev.marshmallows.dao.entity.GoodEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface GoodsRepository extends CrudRepository<GoodEntity, Integer> {

    Stream<GoodEntity> findGoodsEntitiesByActive(boolean isActive);
}
