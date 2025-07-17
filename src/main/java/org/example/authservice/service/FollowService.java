package org.example.authservice.service;

import org.example.authservice.constant.Mode;
import org.example.authservice.model.dto.follow.FollowRequestClient;
import org.example.authservice.model.entity.Users;
import org.example.authservice.model.repository.FollowsRepository;
import org.example.authservice.model.repository.FollowsRequestsRepository;
import org.example.authservice.model.repository.UsersRepository;
import org.example.authservice.utils.CompareUser;
import org.example.authservice.utils.GenerateFollows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FollowService implements IFollowService {

    @Autowired
    public UsersRepository usersRepository;
    @Autowired
    public FollowsRepository followsRepository;
    @Autowired
    public FollowsRequestsRepository followsRequestsRepository;


    @Override
    public Boolean followUser(FollowRequestClient followRequestClient) {
        Users userRequest = usersRepository.findById
                (followRequestClient.getRequesterId()).orElse(null);

        Users userTarget = usersRepository.findById
                (followRequestClient.getTargetId()).orElse(null);

        boolean checkUser = CompareUser.CompareUserFollow(userRequest, userTarget);
        if (!checkUser) {
            return false;
        }
        assert userRequest != null;
        assert userTarget != null;
        if (userRequest.getId().equals(userTarget.getId())) {
            System.out.println("user don't allow follow yourself");
            return false;
        }
        if (userTarget.getMode().equals(Mode.PRIVATE)) {
            if (userRequest.getId().equals(followRequestClient.getRequesterId())) {
                System.out.println("User not follow yourself !");
                return false;
            }
            GenerateFollows.generateUserRequest
                    (userRequest, userTarget, followsRequestsRepository);
        } else {
            GenerateFollows.generateUserFollow
                    (userRequest, userTarget, followsRepository);
        }
        return true;
    }

    @Override
    public Boolean unfollowUser(FollowRequestClient followRequestClient) {
        Users userRequest = usersRepository.findById(followRequestClient.getRequesterId()).orElse(null);
        Users userTarget = usersRepository.findById(followRequestClient.getTargetId()).orElse(null);
        boolean checkUser = CompareUser.CompareUserFollow(userRequest, userTarget);
        if (!checkUser) {
            return false;
        }
        followsRepository.deleteByFollowerAndFollowing(userRequest, userTarget);
        return true;
    }

    @Override
    public Integer totalFollower(UUID userId) {
        Users  user =  usersRepository.findById(userId).orElse(null);
        if (user == null) {
            return -1;
        }
        return followsRepository.countFollowsByFollower(user);
    }

    @Override
    public Integer totalFollowing(UUID userId) {
        Users  user =  usersRepository.findById(userId).orElse(null);
        if (user == null) {
            return -1;
        }
        return followsRepository.countFollowsByFollowing(user);
    }
}
