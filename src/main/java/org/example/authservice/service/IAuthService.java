package org.example.authservice.service;

import org.example.authservice.model.dto.UsersLogin;
import org.example.authservice.model.dto.UsersRegister;
import org.example.authservice.model.entity.Users;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

public interface IAuthService {
    ResponseEntity<?> login(UsersLogin usersLogin);

    ResponseEntity<?> register(UsersRegister usersRegister);

    ResponseEntity<?> refreshToken(String refreshToken);

    ResponseEntity<?> logout(String accessToken, String refreshToken);

    ResponseEntity<?> userInfor(UUID userId);

    boolean checkPassword(String email, String password);
}
