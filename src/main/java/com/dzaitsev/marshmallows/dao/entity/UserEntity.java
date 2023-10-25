package com.dzaitsev.marshmallows.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "email")
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "password")
    private String password;
    @Column(name = "roles")
    private String roles;
    @Column(name = "verified")
    private boolean verified;


    @PrePersist
    private void prePersist(){
        createDate=LocalDateTime.now();
    }
}
