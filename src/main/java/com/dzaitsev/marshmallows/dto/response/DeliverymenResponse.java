package com.dzaitsev.marshmallows.dto.response;

import com.dzaitsev.marshmallows.dto.User;

import java.util.Collection;

public record DeliverymenResponse(Collection<User> users) {

}
