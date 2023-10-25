package com.dzaitsev.marshmallows.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtSignUpResponse {
    private String token;
    private int verificatonCodeTtl;
}