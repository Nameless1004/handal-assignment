package com.assignment.common.exceptions;

public class NotFoundException extends ApiException {
    public NotFoundException(String message) {
        super(message);
    }
}
