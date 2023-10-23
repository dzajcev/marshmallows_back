package com.dzaitsev.marshmallows.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery implements Serializable {

    private Integer id;
    private LocalDateTime createDate;
    private LocalDate deliveryDate;
    private LocalTime start;
    private LocalTime end;
    private DeliveryStatus deliveryStatus;
    private List<Order> orders = new ArrayList<>();
}
