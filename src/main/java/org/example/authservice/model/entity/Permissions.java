package org.example.authservice.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permissions {
    @Id
    private Integer id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Permission name is required")
    private String name;
}
