package com.dzaitsev.marshmallows.dto;

import com.dzaitsev.marshmallows.dao.entity.OrderLineEntity;
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

    private String comment;

    private String deliveryAddress;

    private String phone;

    private Client client;

    private List<OrderLine> orderLines;

    private Double prePaymentSum;

    private Double paySum;

    private boolean shipped;

    private boolean needDelivery;

    private LocalDateTime completeDate;

    private boolean clientNotificated;

    private OrderStatus orderStatus;

}
