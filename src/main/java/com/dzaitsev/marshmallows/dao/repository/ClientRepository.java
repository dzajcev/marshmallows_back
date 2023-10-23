package com.dzaitsev.marshmallows.dao.repository;

import com.dzaitsev.marshmallows.dao.entity.ClientEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface ClientRepository extends CrudRepository<ClientEntity, Integer> {
    Stream<ClientEntity> findClientEntitiesByActive(boolean isActive);
}
