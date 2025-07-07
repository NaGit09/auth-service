package org.example.authservice.service;

import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IUserService {
    ResponseEntity<?> updateAvatar (UUID userId, String avatarUrl);

}
