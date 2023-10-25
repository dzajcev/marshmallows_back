package com.dzaitsev.marshmallows.service;

import com.dzaitsev.marshmallows.dto.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService {
    UserDetailsService userDetailsService();

    User save(User user);

    Optional<User> findByEmail(String email);


}