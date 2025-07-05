package org.example.authservice.model.repository;

import org.example.authservice.model.entity.Follows;
import org.example.authservice.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FollowsRepository extends JpaRepository<Follows, UUID> {

    void deleteByFollowerAndFollowing(Users follower, Users following);

    Integer countFollowsByFollower(Users follower);

    Integer countFollowsByFollowing(Users following);
}
