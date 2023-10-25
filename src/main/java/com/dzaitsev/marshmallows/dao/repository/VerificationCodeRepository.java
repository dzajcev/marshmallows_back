package com.dzaitsev.marshmallows.dao.repository;

import com.dzaitsev.marshmallows.dao.entity.VerificationCodeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface VerificationCodeRepository extends CrudRepository<VerificationCodeEntity, Integer> {

    List<VerificationCodeEntity> getValidatingCodesByUserIdOrderByCreateDate(Integer userId);

    void deleteAllByUserId(Integer userId);

    void deleteAllByUserIdIn(Collection<Integer> ids);
}
