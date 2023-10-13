package com.dzaitsev.marshmallows.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Good {
    private Integer id;
    private String name;
    private Double price;
    private String description;
    private List<Price> prices;
}
