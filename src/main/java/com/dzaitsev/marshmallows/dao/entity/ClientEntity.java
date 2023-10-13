package com.dzaitsev.marshmallows.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clients")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "default_delivery_address")
    private String defaultDeliveryAddress;

    @Column(name = "phone")
    private String phone;

    @Column(name = "link_channels")
    private String linkChannels;

    @Column(name = "comment")
    private String comment;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "client")
    private List<OrderEntity> orders;

    @PrePersist
    private void prePersist() {
        createDate = LocalDateTime.now();
    }

}
