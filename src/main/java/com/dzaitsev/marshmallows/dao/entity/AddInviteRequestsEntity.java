package com.dzaitsev.marshmallows.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "add_deliverymen_requests")
@NoArgsConstructor
@Getter
@Setter
public class AddInviteRequestsEntity extends AbstractEntity {
    @Column(name = "deliveryman_user_id")
    private Integer deliverymanUserId;
    @Column(name = "accepted_date")
    private LocalDateTime acceptedDate;

    @Builder
    public AddInviteRequestsEntity(Integer id, LocalDateTime createDate, Integer userCreate,
                                   LocalDateTime updateDate, Integer userUpdate, Integer deliverymanUserId, LocalDateTime acceptedDate) {
        super(id, createDate, userCreate, updateDate, userUpdate);
        this.deliverymanUserId = deliverymanUserId;
        this.acceptedDate = acceptedDate;
    }
}
