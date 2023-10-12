package com.dzaitsev.marshmallows.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderLine {

    private Integer id;

    private Integer num;

    private LocalDateTime createDate;

    private Good good;

    private Double price;

    private Boolean done;

    private Integer count;

}
