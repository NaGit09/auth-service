package org.example.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.authservice.model.dto.APIResponse;
import org.example.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/auth-service/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;

    public ResponseEntity<?> getUserProfile() {



        return ResponseEntity.ok(new APIResponse<>(

        ));
    }

}
