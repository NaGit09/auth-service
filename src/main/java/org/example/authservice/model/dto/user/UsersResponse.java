package org.example.authservice.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsersResponse {

    private UserInforResponse user_infor;
    private String token;
    private String refreshToken;
}

