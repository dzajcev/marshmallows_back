package com.dzaitsev.marshmallows.dao.entity;

import com.dzaitsev.marshmallows.dto.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "deadline_date")
    private LocalDate deadline;

    @Column(name = "comment")
    private String comment;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "phone")
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order")
    private List<OrderLineEntity> orderLines = new ArrayList<>();

    @Column(name = "pre_payment_sum")
    private Double prePaymentSum;

    @Column(name = "pay_sum")
    private Double paySum;

    @Column(name = "shipped")
    private boolean shipped;

    @Column(name = "complete_date")
    private LocalDateTime completeDate;

    @Column(name = "need_delivery")
    private boolean needDelivery;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "delivery_id")
    private DeliveryEntity delivery;

    @Column(name = "client_notificated")
    private boolean clientNotificated;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


    @PrePersist
    private void prePersist() {
        createDate = LocalDateTime.now();
    }
}
