package org.example.authservice.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.example.authservice.model.dto.user.UserInforResponse;
import org.example.authservice.model.dto.user.UsersResponse;
import org.example.authservice.model.entity.Users;
import org.example.authservice.utils.GenerateUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final String TOKEN_TYPE = "token_type";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    public String generateToken
            (Users user, long expirationMillis, String tokenType) {

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim(TOKEN_TYPE, tokenType)
                .claim("roles", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken
            (String token, String expectedTokenType) {

        try {
            Claims claims = getClaims(token);
            String tokenType = claims.get(TOKEN_TYPE, String.class);

            if (tokenType == null || !tokenType.equals(expectedTokenType)) {
                log.warn("Token type mismatch or missing: expected [{}], found [{}]", expectedTokenType, tokenType);
                return false;
            }

            return true;

        } catch (JwtException e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public UsersResponse timeOutToken
            (Users user, String token, String refreshToken) {
        UserInforResponse inforResponse = GenerateUser.generateUserInfor(user);

        return new UsersResponse(
                inforResponse,
                generateToken(user, 0, ACCESS_TOKEN),
                generateToken(user, 0, REFRESH_TOKEN)
        );
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor
                (secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

