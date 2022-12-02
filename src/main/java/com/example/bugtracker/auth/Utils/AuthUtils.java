package com.example.bugtracker.auth.Utils;

import jakarta.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AuthUtils {
    public static String getToken(HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring("Bearer ".length());
            return token;
        }
        throw new RuntimeException("Unauthorized Access!");
    }
}
