package org.example.authservice.controller;

import lombok.RequiredArgsConstructor;

import org.example.authservice.model.dto.*;
import org.example.authservice.service.AuthService;
import org.example.authservice.utils.GenerateResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/auth-service/auth") // declare request mapping url
@RequiredArgsConstructor
public class AuthController {
    // DI
    public final AuthService authService;
    // user login with email, password
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsersLogin usersLogin) {
        return authService.login(usersLogin);
    }

    // user registry new account
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsersRegister usersRegister) {
        return authService.register(usersRegister);
    }

    // refresh token if access token is expired!
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> body) {
        if (body == null) {
            return GenerateResponse.generateError(401, "body is null ! ");
        }
        String refreshToken = body.get("refreshToken");
        return authService.refreshToken(refreshToken);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/infor/{id}")
    public ResponseEntity<?> getUserInfo(UUID userId) {
        if (userId == null) {
            return GenerateResponse.generateError(401, "userId is null ! ");
        }
        return authService.userInfor(userId);
    }

    // user logout, remove token after user logout
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody UsersResponse body) {
        if (body == null) {
            return GenerateResponse.generateError(401, "body is null ! ");
        }

        String accessToken = body.getToken();
        String refreshToken = body.getRefreshToken();
        return authService.logout(accessToken, refreshToken);
    }
}
