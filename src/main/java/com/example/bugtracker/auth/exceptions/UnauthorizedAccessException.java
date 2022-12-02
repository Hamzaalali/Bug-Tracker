package com.example.bugtracker.auth.exceptions;

public class UnauthorizedAccessException extends RuntimeException{
    public UnauthorizedAccessException(){
        super("Unauthorized Access!");
    }
}
