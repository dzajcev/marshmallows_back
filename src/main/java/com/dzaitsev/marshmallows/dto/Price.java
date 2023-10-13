package com.dzaitsev.marshmallows.dto;

import com.dzaitsev.marshmallows.dao.entity.GoodEntity;
import com.dzaitsev.marshmallows.dao.entity.OrderLineEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Price {

    private Integer id;

    private LocalDateTime createDate;

    private Double price;

}
