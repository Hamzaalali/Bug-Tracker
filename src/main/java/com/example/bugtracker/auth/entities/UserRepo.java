package com.example.bugtracker.auth.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    public User findUserByEmail(String email);
    public User findUserById(Long id);
}
