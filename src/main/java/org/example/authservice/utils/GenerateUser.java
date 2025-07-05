package org.example.authservice.utils;

import lombok.RequiredArgsConstructor;

import org.example.authservice.constant.Mode;
import org.example.authservice.constant.Role;
import org.example.authservice.model.dto.UserInforResponse;
import org.example.authservice.model.entity.Users;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GenerateUser {
    public static final String DEFAULT_AVATAR =
            "https://res.cloudinary.com/dtnffqndg/image/upload/v1750932513/i9gkkhogvphlfy2t6m06.jpg";

    public static Users generateUserRegister(String email, String username, String password, Role defaultRole) {
        return Users.builder()
                .id(UUID.randomUUID())
                .email(email)
                .username(username)
                .passwordHash(password)
                .active(false)
                .role(defaultRole)
                .fullName(username)
                .mode(Mode.PUBLIC)
                .avatarUrl(DEFAULT_AVATAR)
                .build();
    }

    public static UserInforResponse generateUserInfor(Users users) {
        UserInforResponse user_infor = new UserInforResponse();
        user_infor.setUserId(users.getId());
        user_infor.setFull_name(users.getFullName());
        user_infor.setAvatar_url(users.getAvatarUrl());
        user_infor.setEmail(users.getEmail());
        user_infor.setUsername(users.getUsername());
        user_infor.setRole(users.getRole());
        return user_infor;
    }
}
