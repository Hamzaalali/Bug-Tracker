package com.example.bugtracker.auth.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
@Data
public class LoginRequest {
    @NotEmpty(message = "Email can't be empty")
    String email;
    @NotEmpty (message = "Password can't be empty")
    String password;
}
