package com.dzaitsev.marshmallows.dao.repository;

import com.dzaitsev.marshmallows.dao.entity.ClientEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ClientRepository extends CrudRepository<ClientEntity, Integer> {
    Iterable<ClientEntity> findClientEntitiesByActiveAndUserCreate(boolean isActive, Integer userId);

    Iterable<ClientEntity> findAllByUserCreate(Integer userCreate);

    Optional<ClientEntity> findByIdAndUserCreate(Integer id, Integer userCreate);
}
