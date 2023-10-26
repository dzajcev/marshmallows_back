package com.dzaitsev.marshmallows.dao.entity;

import com.dzaitsev.marshmallows.dto.User;
import com.dzaitsev.marshmallows.exceptions.AuthorizationException;
import com.dzaitsev.marshmallows.exceptions.ErrorCodes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "user_create_id")
    private Integer userCreate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "user_update_id")
    private Integer userUpdate;

    @PrePersist
    private void prePersist() {
        createDate = LocalDateTime.now();
        userCreate = getUserFromContext().getId();
    }


    @PreUpdate
    private void preUpdate() {
        updateDate = LocalDateTime.now();
        userUpdate = getUserFromContext().getId();
    }


    private User getUserFromContext() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(m -> (User) m.getPrincipal())
                .orElseThrow(() -> new AuthorizationException(ErrorCodes.AUTH004, ErrorCodes.AUTH004.getText()));
    }
}
