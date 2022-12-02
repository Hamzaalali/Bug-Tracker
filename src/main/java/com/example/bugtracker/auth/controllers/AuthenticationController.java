package com.example.bugtracker.auth.controllers;

import com.example.acm_backend.auth.annotations.Authenticate;
import com.example.acm_backend.auth.annotations.Authorize;
import com.example.acm_backend.auth.entities.User;
import com.example.acm_backend.auth.exceptions.*;
import com.example.acm_backend.auth.requests.LoginRequest;
import com.example.acm_backend.auth.requests.RefreshRequest;
import com.example.acm_backend.auth.requests.RegisterRequest;
import com.example.acm_backend.auth.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Authorize(roles = {"ADMIN"})
    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest registerRequest) throws EmailAlreadyExistsException, InvalidRoleIdException {
        User RegisteredUser=authenticationService.registerUser(registerRequest);
        return new ResponseEntity<>(RegisteredUser, HttpStatus.ACCEPTED);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody() LoginRequest loginRequest) throws Exception {
        Map<String ,String> refreshAndAccess=authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return new ResponseEntity<>(refreshAndAccess, HttpStatus.ACCEPTED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> refresh(@Valid @RequestBody RefreshRequest refreshRequest) throws InvalidRefreshTokenException, UnauthorizedAccessException {
        Map<String ,String> accessTokenObject=authenticationService.refresh(refreshRequest.getRefreshToken());
        return new ResponseEntity<>(accessTokenObject,HttpStatus.ACCEPTED);
    }

    @Authenticate
    @Authorize(roles = {"ADMIN"})
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) throws FailedToLogoutException {
        User user= (User) request.getAttribute("user");
        authenticationService.logout(user);
        return new ResponseEntity<>(Collections.emptyList(),HttpStatus.ACCEPTED);
    }
}
