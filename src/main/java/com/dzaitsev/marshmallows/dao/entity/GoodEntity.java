package com.dzaitsev.marshmallows.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "goods")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodEntity extends AbstractEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "description")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "good")
    private List<PriceEntity> prices;


    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, mappedBy = "good")
    private List<OrderLineEntity> orderLines;

    @Builder
    public GoodEntity(Integer id, LocalDateTime createDate, Integer userCreate, LocalDateTime updateDate, Integer userUpdate,
                      String name, boolean active, String description, List<PriceEntity> prices, List<OrderLineEntity> orderLines) {
        super(id, createDate, userCreate, updateDate, userUpdate);
        this.name = name;
        this.active = active;
        this.description = description;
        this.prices = prices;
        this.orderLines = orderLines;
    }
}
