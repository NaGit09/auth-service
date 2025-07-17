package org.example.authservice.service;

import lombok.RequiredArgsConstructor;
import org.example.authservice.constant.Role;
import org.example.authservice.exception.EmailAlreadyExistsException;
import org.example.authservice.exception.InvalidTokenException;
import org.example.authservice.exception.NotFoundException;
import org.example.authservice.exception.PasswordNotMatchException;
import org.example.authservice.jwt.JwtUtils;
import org.example.authservice.model.dto.auth.UsersLogin;
import org.example.authservice.model.dto.auth.UsersRegister;
import org.example.authservice.model.dto.user.UserInforResponse;
import org.example.authservice.model.dto.user.UsersResponse;
import org.example.authservice.model.entity.Users;
import org.example.authservice.model.repository.UsersRepository;
import org.example.authservice.utils.GenerateUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
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
    public UsersResponse register(UsersRegister usersRegister) {
        String email = usersRegister.getEmail();
        String username = usersRegister.getUsername();
        String rawPassword = usersRegister.getPassword();

        if (usersRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);

        Users user = GenerateUser.generateUserRegister(
                email, username, encodedPassword, Role.USER
        );

        user = usersRepository.save(user);

        String accessToken = jwtUtils.generateToken(
                user, expirationTime, "access_token");

        String refreshToken = jwtUtils.generateToken(
                user, refreshTime, "refresh_token");

        UserInforResponse userInfor = GenerateUser.generateUserInfor(user);

        return new UsersResponse(userInfor, accessToken, refreshToken);
    }

    @Override
    public Map<String, String> refreshToken(String refreshToken) {

        if (refreshToken == null ||
                !jwtUtils.validateToken
                        (refreshToken, "refresh_token")) {
          throw new InvalidTokenException(refreshToken);
        }

        String userId = jwtUtils.getUserIdFromToken(refreshToken);

        Users user = usersRepository.findById
                (UUID.fromString(userId)).orElseThrow(() ->
                new NotFoundException(userId));

        String newAccessToken = jwtUtils.generateToken
                (user, expirationTime, "access_token");

        return Map.of("accessToken", newAccessToken);
    }

    @Override
    public UsersResponse logout(String accessToken, String refreshToken) {

        if (refreshToken == null || accessToken == null ||
                !jwtUtils.validateToken
                        (refreshToken, "refresh_token")

                || !jwtUtils.validateToken
                (accessToken, "access_token")) {

          throw new InvalidTokenException(accessToken);
        }

        UUID uuid = UUID.fromString(jwtUtils.getUserIdFromToken(accessToken));
        Users user = usersRepository.findById(uuid).orElseThrow(() ->
                new NotFoundException(uuid.toString()));

        return jwtUtils.timeOutToken(user, accessToken, refreshToken);

    }

    @Override
    public UsersResponse login(UsersLogin usersLogin) {

        // Phase 1: Authentication user
        String email = usersLogin.getEmail();
        String password = usersLogin.getPassword();

        if (!usersRepository.existsByEmail(email)) {
           throw new EmailAlreadyExistsException(email);
        }
        if (!checkPassword(email, password)) {
            throw new PasswordNotMatchException(password);
        }
        Users user = usersRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(email)
        );

        // Phase 2: Generate tokens and save them into a database
        String accessToken = jwtUtils.generateToken(user, expirationTime, "access_token");
        String refreshToken = jwtUtils.generateToken(user, refreshTime, "refresh_token");
        UserInforResponse user_infor = GenerateUser.generateUserInfor(user);

        // Phase 3: Response with status, message, and tokens
        return new UsersResponse(user_infor, accessToken, refreshToken);
    }

    public boolean checkPassword(String email, String rawPassword) {

        String encodedPassword = usersRepository.getPasswordByEmail(email);

        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
