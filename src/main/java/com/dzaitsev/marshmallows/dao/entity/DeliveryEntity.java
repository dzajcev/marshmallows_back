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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "create_date")
    private LocalDateTime createDate;
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

    @PrePersist
    private void prePersist() {
        createDate = LocalDateTime.now();
    }

    public DeliveryStatus getDeliveryStatus() {
        if (getOrders() != null && (getOrders().stream().allMatch(OrderEntity::isShipped))) {
            return DeliveryStatus.DONE;
        } else if (getOrders() != null && (getOrders().stream().anyMatch(f -> !f.isShipped())
                && getOrders().stream().anyMatch(OrderEntity::isShipped))) {
            return DeliveryStatus.IN_PROGRESS;
        } else {
            return DeliveryStatus.NEW;
        }
    }
}
