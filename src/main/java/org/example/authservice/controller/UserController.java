package org.example.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.authservice.model.dto.APIResponse;
import org.example.authservice.model.dto.user.UserInforResponse;
import org.example.authservice.model.dto.user.UserUpdateAvatar;
import org.example.authservice.service.UserService;
import org.example.authservice.utils.GenerateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth-service/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;

    @GetMapping("/infor/{id}")
    public ResponseEntity<?> getUserInfo
            (@PathVariable UUID id) {

        UserInforResponse userInforResponse = userService.userInfor(id);
        return GenerateResponse.generateSuccess
                (200, "get user infor successfully", userInforResponse);
    }

    @PutMapping("/update-avatar")
    public ResponseEntity<?> updateAvatar
            (@RequestBody UserUpdateAvatar userUpdateAvatar) {

        UUID userId = userUpdateAvatar.getUserId();
        String avatarUrl = userUpdateAvatar.getAvatarUrl();

        UserInforResponse userInforResponse = userService.updateAvatar(userId, avatarUrl);

        return GenerateResponse.generateSuccess
                (200, "updateAvatar success", userInforResponse);
    }

    @PostMapping("/profile")
    public ResponseEntity<?> getUserProfile() {


        return ResponseEntity.ok(new APIResponse<>(

        ));
    }

    @PostMapping("/save-post")
    public ResponseEntity<?> savePost() {


        return ResponseEntity.ok(new APIResponse<>(

        ));
    }


}
