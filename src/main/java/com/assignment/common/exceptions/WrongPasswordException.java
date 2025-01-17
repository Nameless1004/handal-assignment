package com.assignment.common.exceptions;

public class WrongPasswordException extends ApiException {
    public WrongPasswordException() {
        super("Wrong Password");
    }

}
