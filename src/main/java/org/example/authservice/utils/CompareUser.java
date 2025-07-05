package org.example.authservice.utils;

import org.example.authservice.model.entity.Users;

public class CompareUser {
    public static boolean CompareUserFollow (Users userRequest , Users userTarget) {
        if (userRequest == null || userTarget == null) {
            System.out.println("Users not found");
            return false;
        }
        return true;
    }
}
