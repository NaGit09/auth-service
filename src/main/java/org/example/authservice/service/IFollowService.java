package org.example.authservice.service;

import org.example.authservice.model.dto.follow.FollowRequestClient;

import java.util.UUID;

public interface IFollowService {
    Boolean followUser(FollowRequestClient followRequestClient);

    Boolean unfollowUser(FollowRequestClient followRequestClient);

    Integer totalFollower(UUID userId);

    Integer totalFollowing(UUID userId);
}
