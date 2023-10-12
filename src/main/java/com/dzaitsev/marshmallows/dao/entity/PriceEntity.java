package com.dzaitsev.marshmallows.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "prices")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "good_id")
    private GoodEntity good;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "realPrice")
    private List<OrderLineEntity> orderLines;

    @Column(name = "price")
    private Double price;


    @PrePersist
    private void prePersist(){
        createDate=LocalDateTime.now();
    }
}
