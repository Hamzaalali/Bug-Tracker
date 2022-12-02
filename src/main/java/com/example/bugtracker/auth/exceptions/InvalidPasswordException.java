package com.example.bugtracker.auth.exceptions;

public class InvalidPasswordException extends MyCustomExceptions{

    public InvalidPasswordException(){
        super("Invalid Password!");
    }
}
