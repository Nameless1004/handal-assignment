package com.assignment.domain.auth.service;

import com.assignment.domain.auth.dto.AuthRequestDto;
import com.assignment.domain.auth.dto.AuthResponseDto;

public interface AuthService {
    AuthResponseDto.Signup signup(AuthRequestDto.Signup request);
    AuthResponseDto.Signin signin(AuthRequestDto.Signin request);
}
