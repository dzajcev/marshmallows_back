package com.dzaitsev.marshmallows.mappers;

import java.util.List;

public abstract class Mapper<DTO, ENTITY> {

    public abstract DTO toDto(ENTITY entity);

    public List<DTO> toDto(List<ENTITY> entities) {
        return entities.stream()
                .map(this::toDto).toList();
    }

}
