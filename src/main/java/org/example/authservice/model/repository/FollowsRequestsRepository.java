package org.example.authservice.model.repository;

import org.example.authservice.model.entity.FollowRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FollowsRequestsRepository extends JpaRepository<FollowRequest , UUID> {

}
