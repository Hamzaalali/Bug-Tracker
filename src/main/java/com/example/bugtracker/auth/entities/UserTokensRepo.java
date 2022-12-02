package com.example.bugtracker.auth.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokensRepo extends JpaRepository<UserTokens,Long> {
    public UserTokens findUserTokensByUser(User user);
}
