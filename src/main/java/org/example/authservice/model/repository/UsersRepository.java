package org.example.authservice.model.repository;

import org.example.authservice.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UsersRepository extends JpaRepository<Users, UUID> {
    // custom query
    @Query("SELECT u.password_hash FROM Users u WHERE u.email = :email")
    String getPasswordByEmail(@Param("email") String email);

    boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);
    Optional<Users> findById(UUID id);
}
