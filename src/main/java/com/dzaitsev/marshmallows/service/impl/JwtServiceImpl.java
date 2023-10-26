package com.dzaitsev.marshmallows.service.impl;

import com.dzaitsev.marshmallows.dao.entity.JwtTokenEntity;
import com.dzaitsev.marshmallows.dao.repository.JwtTokenRepository;
import com.dzaitsev.marshmallows.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${authorization.signing.key}")
    private String jwtSigningKey;

    private final JwtTokenRepository jwtTokenRepository;

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(UserDetails userDetails, TokenType tokenType) {
        LocalDateTime expirationDate = LocalDateTime.now().plusSeconds(tokenType.getTtl() * 60L);
        String token = Jwts.builder().claims()
                .subject(userDetails.getUsername())
                .expiration(Date
                        .from(expirationDate.atZone(ZoneId.systemDefault())
                                .toInstant()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .add("token_type", tokenType)
                .and()
                .signWith(getSigningKey()).compact();

        jwtTokenRepository.save(new JwtTokenEntity(token, userDetails.getUsername(), expirationDate));
        return token;
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    public TokenType extractTokenType(String token) {
        return extractClaim(token, claims -> Optional.ofNullable(claims.get("token_type", String.class))
                .map(TokenType::valueOf)
                .orElseThrow(() -> new RuntimeException("token type not defined")));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}