package org.example.authservice.service;

import org.example.authservice.model.dto.UsersRegister;
import org.example.authservice.model.entity.Users;

import java.util.Optional;

public interface IAuthService {
    public Optional<Users> login(String email , String password);
    public boolean register(UsersRegister usersRegister);
    public boolean checkPassword(String email, String password);
    public boolean checkEmail(String email);
}
