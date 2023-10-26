package com.dzaitsev.marshmallows.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
public class ClientEntity extends AbstractEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "default_delivery_address")
    private String defaultDeliveryAddress;

    @Column(name = "phone")
    private String phone;

    @Column(name = "link_channels")
    private String linkChannels;

    @Column(name = "comment")
    private String comment;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "client")
    private List<OrderEntity> orders;

    @Builder
    public ClientEntity(Integer id, LocalDateTime createDate, Integer userCreate, LocalDateTime updateDate, Integer userUpdate,
                        String name, boolean active, String defaultDeliveryAddress, String phone, String linkChannels, String comment, List<OrderEntity> orders) {
        super(id, createDate, userCreate, updateDate, userUpdate);
        this.name = name;
        this.active = active;
        this.defaultDeliveryAddress = defaultDeliveryAddress;
        this.phone = phone;
        this.linkChannels = linkChannels;
        this.comment = comment;
        this.orders = orders;
    }

}
