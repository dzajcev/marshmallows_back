package com.dzaitsev.marshmallows.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class InviteRequest {
    private Integer id;
    private User user;
    private LocalDateTime createDate;
    private LocalDateTime acceptDate;

}
