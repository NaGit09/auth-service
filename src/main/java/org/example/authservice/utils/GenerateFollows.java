package org.example.authservice.utils;

import org.example.authservice.constant.Status;
import org.example.authservice.model.entity.FollowRequest;
import org.example.authservice.model.entity.Follows;
import org.example.authservice.model.entity.Users;
import org.example.authservice.model.repository.FollowsRepository;
import org.example.authservice.model.repository.FollowsRequestsRepository;

import java.util.UUID;

public class GenerateFollows {
    public static void generateUserRequest(Users userRequest, Users userTarget,
                                           FollowsRequestsRepository followsRequestsRepository) {
        FollowRequest request = new FollowRequest();
        request.setId(UUID.randomUUID());
        request.setTarget(userTarget);
        request.setRequester(userRequest);
        request.setStatus(Status.PENDING);
        followsRequestsRepository.save(request);
    }

    public static void generateUserFollow(Users userRequest, Users userTarget,
                                          FollowsRepository followsRepository) {
        Follows follow = new Follows();
        follow.setFollower(userRequest);
        follow.setFollowing(userTarget);
        followsRepository.save(follow);
    }
}
