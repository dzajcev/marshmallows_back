package com.dzaitsev.marshmallows.dao.entity;

import com.dzaitsev.marshmallows.dto.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryEntity extends AbstractEntity implements Serializable {

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "start_time")
    private LocalTime start;

    @Column(name = "end_time")
    private LocalTime end;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "delivery")
    private List<OrderEntity> orders = new ArrayList<>();

    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor_id")
    private UserEntity executor;

    @Builder
    public DeliveryEntity(Integer id, LocalDateTime createDate, Integer userCreate, LocalDateTime updateDate, Integer userUpdate,
                          LocalDate deliveryDate, LocalTime start, LocalTime end, List<OrderEntity> orders, DeliveryStatus deliveryStatus) {
        super(id, createDate, userCreate, updateDate, userUpdate);
        this.deliveryDate = deliveryDate;
        this.start = start;
        this.end = end;
        this.orders = orders;
        this.deliveryStatus = deliveryStatus;
    }
}
