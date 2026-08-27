package by.oleg.homehub.service.impl;

import by.oleg.homehub.entity.User;
import by.oleg.homehub.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    @Override
    public String generateJwtToken(User user) {
        log.info("generateJwtToken");
        return Jwts.builder()
                .claim("role", user.getRole())
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignedKey())
                .compact();
    }

    private SecretKey getSignedKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSignedKey()).build().parseSignedClaims(token).getPayload();
    }
}
