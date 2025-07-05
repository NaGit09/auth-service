package org.example.authservice.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.authservice.model.entity.CustomUserDetails;
import org.example.authservice.model.entity.Users;
import org.example.authservice.model.repository.UsersRepository;
import org.example.authservice.service.CustomUserDetailsService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@NonNullApi
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtil;
    private final UsersRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth-service/auth/login", "/auth-service/auth/register", "/auth-service/auth/refresh-token"
    );

    // filter request
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // skip request is not authenticated
        String path = request.getServletPath();
        if (PUBLIC_PATHS.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }
        // check header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            handleError(response, "Missing token");
            return;
        }

        String token = authHeader.substring(7);
        // check token type
        try {
            if (!jwtUtil.validateToken(token, "access_token")) {
                handleError(response, "Invalid token");
                return;
            }

            String userId = jwtUtil.getUserIdFromToken(token);

            Optional<Users> users = userRepository.findById(UUID.fromString(userId));
            Users user = users.orElse(null);

            // required, if not this 403 error.
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                assert user != null;
                // get user with UserDetailService
                CustomUserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
                // authentication and authorization with user entity
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken
                                (userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // add auth token in Security context holder
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (JwtException e) {
            handleError(response, "Token error: " + e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void handleError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Create a map with the status and message
        Map<String, Object> errorResponse = Map.of("status", HttpServletResponse.SC_UNAUTHORIZED, "message", message);

        // Convert the map to a JSON string using ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        // Write the JSON response
        response.setContentType("application/json");
        response.getWriter().println(jsonResponse);
    }
}

