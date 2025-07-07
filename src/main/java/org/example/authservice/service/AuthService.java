package org.example.authservice.service;

import lombok.RequiredArgsConstructor;
import org.example.authservice.constant.Role;
import org.example.authservice.jwt.JwtUtils;
import org.example.authservice.model.dto.*;
import org.example.authservice.model.entity.Users;
import org.example.authservice.model.repository.UsersRepository;
import org.example.authservice.utils.GenerateResponse;
import org.example.authservice.utils.GenerateUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    public final UsersRepository usersRepository;
    public final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public long expirationTime = 1000 * 60 * 60 * 15;
    public long refreshTime = 1000 * 60 * 60 * 24 * 7;

    @Override
    public ResponseEntity<?> register(UsersRegister usersRegister) {
        String email = usersRegister.getEmail();
        String username = usersRegister.getUsername();
        String rawPassword = usersRegister.getPassword();

        if (usersRepository.existsByEmail(email)) {
            return GenerateResponse.generateError(401, "email already existed !");
        }
        String encodedPassword = passwordEncoder.encode(rawPassword);

        Users u = GenerateUser.generateUserRegister(email, username, encodedPassword, Role.USER);
        usersRepository.save(u);

        Optional<Users> user = usersRepository.findByEmail(email);
        UUID userId = user.get().getId();

        if (userId == null) {
            return GenerateResponse.generateError(401, "register failed !");
        }

        String accessToken = jwtUtils.generateToken(user.get(), expirationTime, "access_token");
        String refreshToken = jwtUtils.generateToken(user.get(), refreshTime, "refresh_token");
        UserInforResponse user_infor = GenerateUser.generateUserInfor(user.get());

        return GenerateResponse.generateSuccess(200, "Register  successfully",
                new UsersResponse(user_infor, accessToken, refreshToken));
    }

    @Override
    public ResponseEntity<?> refreshToken(String refreshToken) {

        if (refreshToken == null || !jwtUtils.validateToken(refreshToken, "refresh_token")) {
            return GenerateResponse.generateError(401, "Refresh token timeout");
        }

        String userId = jwtUtils.getUserIdFromToken(refreshToken);
        Optional<Users> user = usersRepository.findById(UUID.fromString(userId));

        if (user.isEmpty()) {
            return GenerateResponse.generateError(401, "user not found ");
        }
        String newAccessToken = jwtUtils.generateToken(user.get(), expirationTime, "access_token");
        return GenerateResponse.generateSuccess(200, "refresh token successfully !",
                Map.of("accessToken", newAccessToken));
    }

    @Override
    public ResponseEntity<?> logout(String accessToken, String refreshToken) {

        if (refreshToken == null || accessToken == null ||
                !jwtUtils.validateToken(refreshToken, "refresh_token")
                || !jwtUtils.validateToken(accessToken, "access_token")) {
            return GenerateResponse.generateError(401, "refresh invalid token");
        }

        UUID uuid = UUID.fromString(jwtUtils.getUserIdFromToken(accessToken));
        Optional<Users> user = usersRepository.findById(uuid);

        return GenerateResponse.generateSuccess(200, "logout successfully !",
                jwtUtils.TimeOutToken(user.get(), accessToken, refreshToken));
    }

    @Override
    public ResponseEntity<?> userInfor(UUID userId) {
        Optional<Users> user = usersRepository.findById(userId);
        if (user.isEmpty()) {
            return GenerateResponse.generateError(401, "user not found ");
        }
        UserInforResponse user_infor = GenerateUser.generateUserInfor(user.get());
        return GenerateResponse.generateSuccess(200, "get user info successfully !", user_infor);
    }

    @Override
    public ResponseEntity<?> login(UsersLogin usersLogin) {

        // Phase 1: Authentication user
        String email = usersLogin.getEmail();
        String password = usersLogin.getPassword();

        if (!usersRepository.existsByEmail(email)) {
            return GenerateResponse.generateError(401, "email not existed !");
        }
        if (!checkPassword(email, password)) {
            return GenerateResponse.generateError(401, " password incorrect");
        }
        Optional<Users> user = usersRepository.findByEmail(email);

        // Phase 2: Generate tokens and save them into a database
        String accessToken = jwtUtils.generateToken(user.get(), expirationTime, "access_token");
        String refreshToken = jwtUtils.generateToken(user.get(), refreshTime, "refresh_token");
        UserInforResponse user_infor = GenerateUser.generateUserInfor(user.get());

        // Phase 3: Response with status, message, and tokens
        return GenerateResponse.generateSuccess(200, "Login successfully",
                new UsersResponse(user_infor, accessToken, refreshToken));
    }

    public boolean checkPassword(String email, String rawPassword) {
        String encodedPassword = usersRepository.getPasswordByEmail(email);
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
