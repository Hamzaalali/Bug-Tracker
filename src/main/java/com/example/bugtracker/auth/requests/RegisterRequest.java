package com.example.bugtracker.auth.requests;

import com.example.acm_backend.auth.entities.Role;
import jakarta.persistence.Column;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class RegisterRequest {
    @NotEmpty(message = "Username can't be empty")
    private String userName;
    @Column(unique=true)
    @NotEmpty(message = "Email can't be empty")
    private String email;
    @NotEmpty(message = "Password can't be empty")
    @Size(min = 3, max = 15)
    private String password;
    @NotEmpty(message = "Roles can't be empty")
    private List<Role> roles;
}
