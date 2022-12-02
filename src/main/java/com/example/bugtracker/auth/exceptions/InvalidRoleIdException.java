package com.example.bugtracker.auth.exceptions;

public class InvalidRoleIdException extends MyCustomExceptions{
    public InvalidRoleIdException(){
        super("Invalid Role ID!");
    }
}
