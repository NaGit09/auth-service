package org.example.authservice.utils;

import lombok.RequiredArgsConstructor;

import org.example.authservice.model.dto.UserInforResponse;
import org.example.authservice.model.entity.Roles;
import org.example.authservice.model.entity.Users;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
@RequiredArgsConstructor
public class GenerateUser {
    public static final String DEFAULT_AVATAR = "https://res.cloudinary.com/dtnffqndg/image/upload/v1750932513/i9gkkhogvphlfy2t6m06.jpg";
    public static Users generateUserRegister(String email, String username, String password, Roles defaultRole) {

        return Users.builder()
                .id(UUID.randomUUID())
                .email(email)
                .username(username)
                .password_hash(password)
                .active(false)
                .role(defaultRole)

                .full_name(username)
                .avatar_url(DEFAULT_AVATAR)
                .build();
    }

    public static UserInforResponse generateUserInfor(Users  users) {
        UserInforResponse user_infor = new UserInforResponse();
        user_infor.setFull_name(users.getFull_name());
        user_infor.setAvatar_url(users.getAvatar_url());
        user_infor.setEmail(users.getEmail());
        user_infor.setUsername(users.getUsername());
        user_infor.setRole(users.getRole().getName());
        user_infor.setPermissions(users.getRole().getPermissions());
        return user_infor;
    }
}
