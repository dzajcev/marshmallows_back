package com.dzaitsev.marshmallows.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Integer id;

    private LocalDateTime createDate;

    private LocalDate deadline;

    private LinkChannel linkChannel;

    private String comment;

    private String deliveryAddress;

    private Client client;

    private List<OrderLine> orderLines;

    private Double prePaymentSum;

    private Boolean shipped;

    private LocalDateTime completeDate;

}
