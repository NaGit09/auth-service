package org.example.authservice.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class FollowId implements Serializable {
    private UUID follower;
    private UUID following;

}
