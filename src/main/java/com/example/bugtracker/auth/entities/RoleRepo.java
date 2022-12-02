package com.example.bugtracker.auth.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface  RoleRepo extends JpaRepository<Role, Long> {
    public Role findRoleById(Long id);
}
