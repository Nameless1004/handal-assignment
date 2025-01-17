package com.assignment.exceptions;

public class DuplicatedException extends ApiException {
    public DuplicatedException(String message) {
        super(message);
    }
}
