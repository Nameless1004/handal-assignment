package com.assignment.dto;

import com.assignment.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public sealed interface AuthResponseDto permits AuthResponseDto.Signup, AuthResponseDto.Signin {
    record Signup(String username, String nickname, Collection<? extends GrantedAuthority> authorities) implements AuthResponseDto {
        public Signup(User user) {
            this(user.getUsername(), user.getNickname(), List.of(new SimpleGrantedAuthority(user.getRole())));
        }
    }

    record Signin(String accessToken, String refreshToken) implements AuthResponseDto { }
}
