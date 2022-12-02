package com.example.bugtracker.auth.exceptions;

public class EmailAlreadyExistsException extends MyCustomExceptions{
    public EmailAlreadyExistsException(){
        super("Email Already Exists!");
    }
}
