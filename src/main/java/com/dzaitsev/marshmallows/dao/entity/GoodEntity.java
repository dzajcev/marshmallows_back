package com.dzaitsev.marshmallows.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "goods")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "description")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "good")
    private List<PriceEntity> prices;


    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, mappedBy = "good")
    private List<OrderLineEntity> orderLines;


    @PrePersist
    private void prePersist() {
        createDate = LocalDateTime.now();
    }
}
