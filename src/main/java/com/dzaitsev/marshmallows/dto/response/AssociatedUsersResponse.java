package com.dzaitsev.marshmallows.dto.response;

import com.dzaitsev.marshmallows.dto.User;

import java.util.Collection;

public record AssociatedUsersResponse(Collection<User> users) {

}
