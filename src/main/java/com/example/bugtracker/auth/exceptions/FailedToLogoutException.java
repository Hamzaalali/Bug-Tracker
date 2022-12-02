package com.example.bugtracker.auth.exceptions;

public class FailedToLogoutException extends MyCustomExceptions{
    public FailedToLogoutException(){
        super("Logout Failed!");
    }
}
