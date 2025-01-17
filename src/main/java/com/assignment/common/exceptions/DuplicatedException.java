package com.assignment.common.exceptions;

public class DuplicatedException extends ApiException {
    public DuplicatedException(String message) {
        super(message);
    }
}
