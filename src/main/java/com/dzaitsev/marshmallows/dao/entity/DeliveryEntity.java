package com.dzaitsev.marshmallows.dao.entity;

import com.dzaitsev.marshmallows.dto.Order;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryEntity implements Serializable {
    @Id
    private Integer id;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "delivery_date")
    private LocalDate deliveryDate;
    @Column(name = "start_time")
    private LocalTime start;
    @Column(name = "end_time")
    private LocalTime end;
    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, mappedBy = "delivery")
    private List<OrderEntity> orders = new ArrayList<>();
}
