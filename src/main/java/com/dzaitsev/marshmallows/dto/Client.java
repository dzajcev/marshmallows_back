package com.dzaitsev.marshmallows.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private Integer id;

    private String name;

    private LocalDateTime createDate;

    private String defaultDeliveryAddress;

    private String phone;

    private String comment;
    private List<LinkChannel> linkChannels;


}
