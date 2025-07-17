package org.example.authservice.service;

import org.example.authservice.exception.NotFoundException;
import org.example.authservice.model.dto.user.UserInforResponse;
import org.example.authservice.model.entity.Users;
import org.example.authservice.model.repository.UsersRepository;
import org.example.authservice.utils.GenerateUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements IUserService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserInforResponse userInfor
            (UUID userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("User not found with ID: " + userId));

        return GenerateUser.generateUserInfor(user);
    }

    @Override
    public UserInforResponse updateAvatar
            (UUID userId, String avatarUrl) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("User not found with ID: " + userId));

        user.setAvatarUrl(avatarUrl);
        Users updatedUser = usersRepository.save(user);

        return GenerateUser.generateUserInfor(updatedUser);
    }

}
