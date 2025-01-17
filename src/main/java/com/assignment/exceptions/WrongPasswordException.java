package com.assignment.exceptions;

public class WrongPasswordException extends ApiException {
    public WrongPasswordException() {
        super("Wrong Password");
    }

}
