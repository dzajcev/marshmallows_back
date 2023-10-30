package com.dzaitsev.marshmallows.dao.entity;

import com.dzaitsev.marshmallows.dto.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "verified")
    private boolean verified;


    @ManyToMany(mappedBy = "developers")
    private Set<UserEntity> deliverymans = new HashSet<>();
    @ManyToMany()
    @JoinTable(name = "developer_deliveryman",
            joinColumns = {@JoinColumn(name = "deliveryman_id")},
            inverseJoinColumns = {@JoinColumn(name = "developer_id")})
    private Set<UserEntity> developers = new HashSet<>();

    //    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "developer_deliveryman",
//            joinColumns = {@JoinColumn(name = "deliveryman_id")},
//            inverseJoinColumns = {@JoinColumn(name = "developer_id")})


    @PrePersist
    private void prePersist() {
        createDate = LocalDateTime.now();
    }
}
