package com.assignment.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ErrorMessage {
    USERNAME_DUPLICATED_MESSAGE("Username is already in use"),
    NICKNAME_DUPLICATED_MESSAGE("Nickname is already in use"),
    WRONG_PASSWORD("Wrong Password");
    private final String message;

    public String get() {
        return message;
    }
}
