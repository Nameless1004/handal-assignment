package com.assignment.common.exceptions;

import com.assignment.common.enums.ErrorMessage;

public class WrongPasswordException extends ApiException {
    public WrongPasswordException() {
        super(ErrorMessage.WRONG_PASSWORD.get());
    }

}
