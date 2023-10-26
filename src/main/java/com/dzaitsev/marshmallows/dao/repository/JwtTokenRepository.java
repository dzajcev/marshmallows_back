package com.dzaitsev.marshmallows.dao.repository;

import com.dzaitsev.marshmallows.dao.entity.JwtTokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface JwtTokenRepository extends CrudRepository<JwtTokenEntity, String> {
    void deleteAllByUserName(String userName);

    void deleteAllByExpireDateBefore(LocalDateTime date);
}
