package org.example.authservice.model.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserInforResponse {
    private String username;
    private String email;
    private String avatar_url;
    private String full_name;
    private String role;

}
