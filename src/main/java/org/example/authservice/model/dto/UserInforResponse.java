package org.example.authservice.model.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.authservice.constant.Role;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserInforResponse {
    private UUID userId;
    private String username;
    private String email;
    private String avatar_url;
    private String full_name;
    private Role role;

}
