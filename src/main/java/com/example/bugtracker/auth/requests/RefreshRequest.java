package com.example.bugtracker.auth.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
@Data
public class RefreshRequest {
    @NotEmpty(message = "Refresh Token Can't Be Empty")
    String refreshToken;
}
