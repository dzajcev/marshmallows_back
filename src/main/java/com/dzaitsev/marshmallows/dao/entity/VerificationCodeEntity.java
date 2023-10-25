package com.dzaitsev.marshmallows.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_codes")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class VerificationCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer codeId;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "ttl")
    private LocalDateTime ttl;
    @Column(name = "code")
    private String code;

    @PrePersist
    private void prePersist(){
        createDate=LocalDateTime.now();
    }
}


