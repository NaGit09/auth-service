package org.example.authservice.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.example.authservice.model.dto.UserInforResponse;
import org.example.authservice.model.dto.UsersResponse;
import org.example.authservice.model.entity.Users;
import org.example.authservice.utils.GenerateUser;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtUtils {

    private static final String SECRET_KEY = "SK40CSI2PC-D#?4klj50fdl3kd3ldư;l3ke";
    private static final String TOKEN_TYPE = "token_type";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final Integer TOKEN_EXPIRATION = 0;

    // generate signature from a private key
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // generate token from userId and time token expired
    public String generateToken(Users users, long TOKEN_EXPIRATION, String type) {
        return Jwts.builder()
                .setSubject(users.getId().toString())
                .claim(TOKEN_TYPE, type)
                .claim("roles", users.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean validateToken(String token, String expectedTokenType) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String tokenType = claims.get("token_type", String.class);
            String role = claims.get("roles", String.class);
            if (tokenType == null) {
                System.out.println("Token không có loại xác định (token_type)");
                return false;
            }

            if (!tokenType.equals(expectedTokenType)) {
                System.out.println("Loại token không khớp: " + tokenType);
                return false;
            }

            return true;

        } catch (ExpiredJwtException e) {
            System.out.println("Token đã hết hạn: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Token không hỗ trợ: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Token bị lỗi: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi tham số token: " + e.getMessage());
        }
        return false;
    }

    // get userId from token return claims
    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Timeout token
    public UsersResponse TimeOutToken(Users users, String token, String refreshToken) {
        UserInforResponse inforResponse = GenerateUser.generateUserInfor(users);
        return new UsersResponse(
                inforResponse,
                generateToken(users, TOKEN_EXPIRATION, ACCESS_TOKEN),
                generateToken(users, TOKEN_EXPIRATION, REFRESH_TOKEN)
        );
    }
}
