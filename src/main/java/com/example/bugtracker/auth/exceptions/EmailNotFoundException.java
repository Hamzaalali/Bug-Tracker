package com.example.bugtracker.auth.exceptions;

public class EmailNotFoundException extends MyCustomExceptions{
    public EmailNotFoundException(){
        super("Email Not Found!");
    }
}
