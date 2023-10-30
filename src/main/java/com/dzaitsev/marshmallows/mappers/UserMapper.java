package com.dzaitsev.marshmallows.mappers;

import com.dzaitsev.marshmallows.dao.entity.UserEntity;
import com.dzaitsev.marshmallows.dto.User;
import com.dzaitsev.marshmallows.dto.UserRole;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class UserMapper extends Mapper<User, UserEntity> {

    @Override
    public User toDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return User.builder()
                .id(userEntity.getId())
                .createDate(userEntity.getCreateDate())
                .role(userEntity.getRole())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .password(userEntity.getPassword())
                .enabled(userEntity.isVerified())
                .build();
    }
}
