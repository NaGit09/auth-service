package org.example.authservice.model.repository;

import org.example.authservice.model.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles , Integer> {
    public Roles findByName(String name);
}