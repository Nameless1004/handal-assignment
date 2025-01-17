package com.assignment.service;

import com.assignment.dto.AuthRequestDto;
import com.assignment.dto.AuthResponseDto;

public interface AuthService {
    AuthResponseDto.Signup signup(AuthRequestDto.Signup request);
    AuthResponseDto.Signin signin(AuthRequestDto.Signin request);
}
