package com.dzaitsev.marshmallows.service;

import com.dzaitsev.marshmallows.dto.User;
import com.dzaitsev.marshmallows.dto.auth.ChangePasswordRequest;
import com.dzaitsev.marshmallows.dto.auth.SaveMyInfoRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService {
    UserDetailsService userDetailsService();

    User save(User user);

    void saveMyInfo(SaveMyInfoRequest request);

    Optional<User> findByEmail(String email);

}