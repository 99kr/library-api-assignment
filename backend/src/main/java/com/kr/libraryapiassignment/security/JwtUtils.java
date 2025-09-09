package com.kr.libraryapiassignment.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;


@Component
public class JwtUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtSecret:mySecretKey}")
    private String jwtSecret;

    @Value("${app.jwtAccessExpirationMs:900000}") // 15 minutes
    private int jwtAccessExpirationMs;

    @Value("${app.jwtRefreshExpirationMs:604800000}") // 7 days
    private int jwtRefreshExpirationMs;

    public String generateAccessToken(Authentication authentication) {
        return generateJwtToken(authentication, jwtAccessExpirationMs, JwtTokenType.ACCESS);
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateJwtToken(authentication, jwtRefreshExpirationMs, JwtTokenType.REFRESH);
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public boolean isValidAccessToken(String token) {
        return getTypeClaimFromToken(token) == JwtTokenType.ACCESS;
    }

    public boolean isValidRefreshToken(String token) {
        return getTypeClaimFromToken(token) == JwtTokenType.REFRESH;
    }

    public int getJwtRefreshExpirationMs() {
        return jwtRefreshExpirationMs;
    }

    private JwtTokenType getTypeClaimFromToken(String token) {
        String type = getAllClaimsFromToken(token).get("type", String.class);
        return JwtTokenType.valueOf(type);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(getSigningKey())
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    private String generateJwtToken(Authentication authentication, int durationMs, JwtTokenType type) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        JwtBuilder jwtBuilder = Jwts.builder()
                                    .setSubject(userPrincipal.getUsername())
                                    .claim("type", type.name())
                                    .setIssuedAt(new Date())
                                    .setExpiration(new Date(System.currentTimeMillis() + durationMs))
                                    .signWith(getSigningKey(), SignatureAlgorithm.HS512);

        if (type == JwtTokenType.ACCESS) {
            //jwtBuilder.claim("authorities", authentication.getAuthorities());
            jwtBuilder.addClaims(Map.of(
                    "authorities", userPrincipal.getAuthorities(),
                    "firstName", userPrincipal.getFirstName(),
                    "lastName", userPrincipal.getLastName()
            ));
        }

        return jwtBuilder.compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
