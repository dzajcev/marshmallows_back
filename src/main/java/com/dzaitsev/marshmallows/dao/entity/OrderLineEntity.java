package com.dzaitsev.marshmallows.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_lines")
@Getter
@Setter
@NoArgsConstructor
public class OrderLineEntity extends AbstractEntity{

    @Column(name = "num")
    private Integer num;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "good_id")
    private GoodEntity good;

    @Column(name = "price")
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_id")
    private PriceEntity realPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Column(name = "done")
    private boolean done;

    @Column(name = "count")
    private Integer count;

    @Builder
    public OrderLineEntity(Integer id, LocalDateTime createDate, Integer userCreate,
                           LocalDateTime updateDate, Integer userUpdate, Integer num,
                           GoodEntity good, Double price, PriceEntity realPrice, OrderEntity order, boolean done, Integer count) {
        super(id, createDate, userCreate, updateDate, userUpdate);
        this.num = num;
        this.good = good;
        this.price = price;
        this.realPrice = realPrice;
        this.order = order;
        this.done = done;
        this.count = count;
    }
}
