package org.example.authservice.controller;

import lombok.RequiredArgsConstructor;

import org.example.authservice.model.dto.auth.UsersLogin;
import org.example.authservice.model.dto.auth.UsersRegister;
import org.example.authservice.model.dto.user.UsersResponse;
import org.example.authservice.service.AuthService;
import org.example.authservice.utils.GenerateResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/auth-service/auth")
@RequiredArgsConstructor
public class AuthController {
    // DI
    public final AuthService authService;

    // user login with email, password
    @PostMapping("/login")
    public ResponseEntity<?> login
    (@RequestBody UsersLogin usersLogin) {
        UsersResponse users = authService.login(usersLogin);
        return GenerateResponse.generateSuccess
                (200, "Login successfully", users);
    }

    // user registry new account
    @PostMapping("/register")
    public ResponseEntity<?> register
    (@RequestBody UsersRegister usersRegister) {
        UsersResponse users = authService.register(usersRegister);
        return GenerateResponse.generateSuccess
                (200, "Register success", users);
    }

    // refresh token if access token is expired!
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken
    (@RequestBody Map<String, String> body) {

        String refreshToken = body.get("refreshToken");
        Map<String, String> refreshTokenMap = authService.refreshToken(refreshToken);
        return GenerateResponse.generateSuccess
                (200, "Refresh token success", refreshTokenMap);
    }

    // user logout, remove token after user logout
    @PreAuthorize("hasAnyRole('USER', 'ADMIN' , 'VIP')")
    @PostMapping("/logout")
    public ResponseEntity<?> logout
    (@RequestBody UsersResponse body) {

        String accessToken = body.getToken();
        String refreshToken = body.getRefreshToken();
        UsersResponse users = authService.logout(accessToken, refreshToken);

        return GenerateResponse.generateSuccess
                (200, "Logout success", users);
    }
}
