package com.dzaitsev.marshmallows.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "prices")
@Getter
@Setter
@NoArgsConstructor
public class PriceEntity extends AbstractEntity{

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "good_id")
    private GoodEntity good;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "realPrice")
    private List<OrderLineEntity> orderLines;

    @Column(name = "price")
    private Double price;

    @Builder
    public PriceEntity(Integer id, LocalDateTime createDate, Integer userCreate, LocalDateTime updateDate, Integer userUpdate,
                       GoodEntity good, List<OrderLineEntity> orderLines, Double price) {
        super(id, createDate, userCreate, updateDate, userUpdate);
        this.good = good;
        this.orderLines = orderLines;
        this.price = price;
    }
}
