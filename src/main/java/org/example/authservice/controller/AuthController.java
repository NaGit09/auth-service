package org.example.authservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.example.authservice.config.RequiresPermission;
import org.example.authservice.jwt.JwtUtils;
import org.example.authservice.model.dto.*;
import org.example.authservice.model.entity.Users;
import org.example.authservice.service.AuthService;
import org.example.authservice.utils.GenerateUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/auth") // declare request mapping url
@RequiredArgsConstructor
public class AuthController {
    // DI
    public final AuthService authService;
    public final JwtUtils jwtUtils;
    // time of token expired
    public long expirationTime = 1000 * 60 * 60 * 15;
    public long refreshTime = 1000 * 60 * 60 * 24 * 7;

    // constant messageResponse
    @RequiresPermission("USER_VIEW")
    @GetMapping("/") // Test application
    public ResponseEntity<?> helloSpring(HttpServletRequest request) {
        return ResponseEntity.ok(new APIResponse<>(
                HttpStatus.OK.value(),
                "SUCCESSFULLY",
                "SUCCESSFULLY"
        ));
    }

    // user login with email, password
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsersLogin usersLogin, HttpServletResponse response) {
        // Phase 1: Authentication user
        String email = usersLogin.getEmail();
        String password = usersLogin.getPassword_hash();
        Optional<Users> user = authService.login(email, password);

        if (user.isEmpty()) {
            // Trả về response lỗi khi không tìm thấy người dùng
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(
HttpStatus.NO_CONTENT.value(),
                            "Incorrect email , password !"
                    ));
        }
        // Phase 2: Generate tokens and save them into database
        UUID userId = user.get().getId();
        String accessToken = jwtUtils.generateToken(user.get(), expirationTime, "access_token");
        String refreshToken = jwtUtils.generateToken(user.get(), refreshTime, "refresh_token");
        UserInforResponse user_infor = GenerateUser.generateUserInfor(user.get());

        // Phase 3: Response with status, message, and tokens
        return ResponseEntity.ok(new APIResponse<>(
                HttpStatus.OK.value(),
                "LOGIN SUCCESSFULLY",
                new UsersResponse(user_infor, accessToken, refreshToken)));
    }

    // user registry new account
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsersRegister usersRegister) {
        boolean success = authService.register(usersRegister);
        if (!success) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(
                            HttpStatus.NO_CONTENT.value(),
                            "Infor invalid !"
                    ));

        }
        return ResponseEntity.ok(new APIResponse<>(
                HttpStatus.OK.value(),
                "REGISTER SUCCESSFULLY",
                ""
        ));
    }

    // refresh token if access token is expired!
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null || !jwtUtils.validateToken(refreshToken, "refresh_token")) {
            System.out.println("Refresh token timeout");

            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            HttpStatus.FORBIDDEN.value(),
                            "Refresh token invalid !"
                    )
            );
        }
        String userId = jwtUtils.getUserIdFromToken(refreshToken);
        Optional<Users> user = authService.findUserById(UUID.fromString(userId));
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            HttpStatus.FORBIDDEN.value(),
                            "User not found !"
                    )
            );
        }
        String newAccessToken = jwtUtils.generateToken(user.get(), expirationTime, "access_token");
        return ResponseEntity.ok(new APIResponse<>(
                HttpStatus.OK.value(),
                "LOGIN SUCCESSFULLY",
                Map.of("accessToken", newAccessToken)
        ));
    }

    // user logout, remove token after user logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody UsersResponse body) {
        String accessToken = body.getToken();
        String refreshToken = body.getRefreshToken();
        if (refreshToken == null || accessToken == null ||
                !jwtUtils.validateToken(refreshToken, "refresh_token")
                || !jwtUtils.validateToken(accessToken, "access_token")) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            HttpStatus.FORBIDDEN.value(),
                            "Refresh token invalid !"
                    )
            );
        }
        UUID uuid = UUID.fromString(jwtUtils.getUserIdFromToken(accessToken));

        Optional<Users> user = authService.findUserById(uuid);
        return ResponseEntity.ok(new APIResponse<>(
                HttpStatus.OK.value(),
                "successfully",
                jwtUtils.TimeOutToken(user.get(), accessToken, refreshToken)
        ));
    }
}
