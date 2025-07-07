package org.example.authservice.controller;

import org.example.authservice.model.dto.FollowRequestClient;
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

        if (!follow) {
            return GenerateResponse.generateError(HttpStatus.NO_CONTENT.value(),
                    "follow failed !");
        }
        return GenerateResponse.generateSuccess(HttpStatus.OK.value(),
                "Follow successfully !", true);
    }

    @PostMapping("/unfollow-user")
    public ResponseEntity<?> unfollowUser
            (@RequestBody FollowRequestClient followRequestClient) {
        boolean unfollow = followService.unfollowUser(followRequestClient);
        if (!unfollow) {
            return GenerateResponse.generateError(HttpStatus.BAD_REQUEST.value(),
                    "Unfollow failed !");
        }
        return GenerateResponse.generateSuccess(HttpStatus.OK.value(),
                "Unfollow successfully !", true);
    }

    @GetMapping("/total-followers/{id}")
    public ResponseEntity<?> totalFollowers(@PathVariable UUID id) {
        Integer total = followService.totalFollower(id);
        if (total < 0) {
            return GenerateResponse.generateError(HttpStatus.NOT_FOUND.value(),
                    "User follower not found");
        }
        return GenerateResponse.generateSuccess(HttpStatus.OK.value(),
                "get total followers successfully !", total);
    }

    @GetMapping("/total-followings/{id}")
    public ResponseEntity<?> totalFollowings(@PathVariable UUID id) {
        Integer total = followService.totalFollowing(id);
        if (total < 0) {
            return GenerateResponse.generateError(HttpStatus.NOT_FOUND.value(),
                    "User following not found!");
        }
        return GenerateResponse.generateSuccess(HttpStatus.OK.value(),
                "get total following successfully !", total);
    }
}
