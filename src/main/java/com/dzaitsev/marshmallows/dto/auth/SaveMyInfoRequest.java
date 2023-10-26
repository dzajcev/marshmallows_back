package com.dzaitsev.marshmallows.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveMyInfoRequest {
    private String firstName;
    private String lastName;
}
