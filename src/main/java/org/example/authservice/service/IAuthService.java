package org.example.authservice.service;

import org.example.authservice.model.dto.auth.UsersLogin;
import org.example.authservice.model.dto.auth.UsersRegister;
import org.example.authservice.model.dto.user.UsersResponse;

import java.util.Map;

public interface IAuthService {
    UsersResponse login(UsersLogin usersLogin);

    UsersResponse register(UsersRegister usersRegister);

    Map<String , String> refreshToken(String refreshToken);

    UsersResponse logout(String accessToken, String refreshToken);

    boolean checkPassword(String email, String password);
}
