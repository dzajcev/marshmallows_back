package com.dzaitsev.marshmallows.dto.response;

import com.dzaitsev.marshmallows.dto.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoResponse {
    private User user;
}