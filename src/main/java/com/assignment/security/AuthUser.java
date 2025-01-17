package com.assignment.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser {

    private Long id;
    private final String username;
    private final String nickname;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String role;

    public AuthUser(String id, String username, String nickname, String role) {
        this.id = Long.parseLong(id);
        this.username = username;
        this.nickname = nickname;
        this.authorities = List.of(new SimpleGrantedAuthority(role));
        this.role = role;
    }
}
