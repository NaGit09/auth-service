package org.example.authservice.controller;

import org.example.authservice.model.dto.follow.FollowRequestClient;
import org.example.authservice.service.FollowService;
import org.example.authservice.utils.GenerateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth-service/follows")
public class FollowController {

    @Autowired
    FollowService followService;


    @PostMapping("/follows-user")
    public ResponseEntity<?> followUser
            (@RequestBody FollowRequestClient followRequestClient) {
        boolean follow = followService.followUser(followRequestClient);

        return GenerateResponse.generateSuccess
                (200, "Follow successfully !", follow);
    }

    @PostMapping("/unfollow-user")
    public ResponseEntity<?> unfollowUser
            (@RequestBody FollowRequestClient followRequestClient) {

        boolean unfollow = followService.unfollowUser(followRequestClient);

        return GenerateResponse.generateSuccess(HttpStatus.OK.value(),
                "Unfollow successfully !", unfollow);
    }

    @GetMapping("/total-followers/{id}")
    public ResponseEntity<?> totalFollowers(@PathVariable UUID id) {

        Integer total = followService.totalFollower(id);

        return GenerateResponse.generateSuccess(HttpStatus.OK.value(),
                "get total followers successfully !", total);
    }

    @GetMapping("/total-followings/{id}")
    public ResponseEntity<?> totalFollowings(@PathVariable UUID id) {
        Integer total = followService.totalFollowing(id);

        return GenerateResponse.generateSuccess(HttpStatus.OK.value(),
                "get total following successfully !", total);
    }
}
