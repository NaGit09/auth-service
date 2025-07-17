package org.example.authservice.service;

import org.example.authservice.model.dto.user.UserInforResponse;

import java.util.UUID;

public interface IUserService {

    UserInforResponse userInfor(UUID userId);

    UserInforResponse updateAvatar (UUID userId, String avatarUrl);

}
