package com.assignment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenType {
    ACCESS(60 *  60 * 1000),
    REFRESH(24 * 60 * 60 * 1000);

    private final long lifeTimeMs;
}
