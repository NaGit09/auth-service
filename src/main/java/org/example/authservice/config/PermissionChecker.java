package org.example.authservice.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PermissionChecker {

    public boolean hasPermission(HttpServletRequest request, String requiredPermission) {
        String header = request.getHeader("X-Permissions");
        if (header == null || header.isEmpty()) return false;

        List<String> userPermissions = Arrays.stream(header.split(","))
                .map(String::trim)
                .toList();

        return userPermissions.contains(requiredPermission);
    }
}