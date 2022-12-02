package com.example.bugtracker.auth.exceptions;

public class InvalidRefreshTokenException extends MyCustomExceptions{
    public InvalidRefreshTokenException() {
        super("Invalid refresh Token!");
    }
}
