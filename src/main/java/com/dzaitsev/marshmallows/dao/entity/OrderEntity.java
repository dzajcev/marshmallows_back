package com.dzaitsev.marshmallows.dao.entity;

import com.dzaitsev.marshmallows.dto.LinkChannel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Column(name = "link_channel")
    @Enumerated(EnumType.STRING)
    private LinkChannel linkChannel;

    @Column(name = "comment")
    private String comment;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order")
    private List<OrderLineEntity> orderLines;

    @Column(name = "pre_payment_sum")
    private Double prePaymentSum;

    @Column(name = "shipped")
    private Boolean shipped;

    @Column(name = "complete_date")
    private LocalDateTime completeDate;

    @PrePersist
    private void prePersist() {
        createDate = LocalDateTime.now();
    }
}
