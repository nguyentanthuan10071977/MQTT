package com.lockerpudo.service;

import com.lockerpudo.domain.UserAccount;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key; private final long expirationHours;
    public JwtService(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration-hours}") long expirationHours) {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); this.expirationHours = expirationHours;
    }
    public String createToken(UserAccount user) {
        Instant now = Instant.now();
        return Jwts.builder().subject(user.getEmail()).claim("userId", user.getId()).claim("role", user.getRole().name())
                .issuedAt(Date.from(now)).expiration(Date.from(now.plus(expirationHours, ChronoUnit.HOURS))).signWith(key).compact();
    }
}