package com.dzaitsev.marshmallows.service;

import com.dzaitsev.marshmallows.dto.InviteRequest;
import com.dzaitsev.marshmallows.dto.InviterRequestDirection;
import com.dzaitsev.marshmallows.dto.User;
import com.dzaitsev.marshmallows.dto.UserRole;
import com.dzaitsev.marshmallows.dto.auth.SaveMyInfoRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDetailsService userDetailsService();

    User save(User user);

    void saveMyInfo(SaveMyInfoRequest request);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Integer id);

    User getUserFromContext();
}