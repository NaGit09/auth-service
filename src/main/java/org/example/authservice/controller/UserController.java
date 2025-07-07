package org.example.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.authservice.model.dto.APIResponse;
import org.example.authservice.service.UserService;
import org.example.authservice.utils.GenerateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth-service/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;

    @GetMapping("/user-info/{id}")
    public ResponseEntity<?> getUsers(@PathVariable String id) {

        return ResponseEntity.ok(new APIResponse<>(

        ));
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

    @PostMapping("/update-avatar/{id}")
    public ResponseEntity<?> updateAvatar(@RequestBody Map<String, String> body) {
        if (body == null) {
            return GenerateResponse.generateError(401, "Body is null");
        }
        String url = body.get("url");
        if (url == null) {
            return GenerateResponse.generateError(401, "url is null");
        }
        return ResponseEntity.ok(new APIResponse<>(

        ));
    }

}
