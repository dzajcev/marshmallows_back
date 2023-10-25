package com.dzaitsev.marshmallows.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    enum TokenType {
        SIGN_IN(1), SIGN_UP(1);
        private final int ttl;

        TokenType(int ttl) {
            this.ttl = ttl;
        }

        public int getTtl() {
            return ttl;
        }
    }


    String extractUserName(String token);

    String generateToken(UserDetails userDetails, TokenType tokenType);

    boolean isTokenValid(String token, UserDetails userDetails);

    TokenType extractTokenType(String token);
}