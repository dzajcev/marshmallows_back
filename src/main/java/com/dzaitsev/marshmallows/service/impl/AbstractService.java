package com.dzaitsev.marshmallows.service.impl;

import com.dzaitsev.marshmallows.dto.User;
import com.dzaitsev.marshmallows.exceptions.AuthorizationException;
import com.dzaitsev.marshmallows.exceptions.ErrorCode;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public abstract class AbstractService {

    protected User getUserFromContext() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(m -> (User) m.getPrincipal())
                .orElseThrow(() -> new AuthorizationException(ErrorCode.AUTH004, ErrorCode.AUTH004.getText()));
    }
}
