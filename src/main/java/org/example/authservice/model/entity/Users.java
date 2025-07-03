package org.example.authservice.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.authservice.constant.MODES;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "Username is required")
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    @NotBlank(message = "Password is required")
    private String password_hash;

    @Column(name = "full_name", length = 100)
    private String full_name;

    @Column(name = "avatar_url", columnDefinition = "TEXT")
    private String avatar_url;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Roles role;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "is_ban", nullable = false)
    private boolean is_ban = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MODES mode = MODES.PUBLIC;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updated_at;

    @PrePersist
    protected void onCreate() {
        this.created_at = this.updated_at = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated_at = LocalDateTime.now();
    }
}
