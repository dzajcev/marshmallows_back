package com.dzaitsev.marshmallows.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "jwt_tokens")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenEntity {
    @Id
    @Column(name = "token")
    private String token;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "expire_at")
    private LocalDateTime expireDate;
}
