package org.example.authservice.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private PermissionChecker checker;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod method) {
            RequiresPermission perm = method.getMethodAnnotation(RequiresPermission.class);
            if (perm != null && !checker.hasPermission(request, perm.value())) {
                response.sendError(HttpStatus.FORBIDDEN.value(), "Không có quyền");
                return false;
            }
        }
        return true;
    }
}