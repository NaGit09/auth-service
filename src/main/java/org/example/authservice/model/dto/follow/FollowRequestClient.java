package org.example.authservice.model.dto.follow;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class FollowRequestClient {
    @NotBlank
    public UUID requesterId;
    @NotBlank
    public UUID targetId;
}