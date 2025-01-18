package com.assignment.domain.auth.dto;

import com.assignment.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public sealed interface AuthResponseDto permits AuthResponseDto.Signup, AuthResponseDto.Signin {
    @Schema(description = "회원가입 응답 DTO")
    record Signup(
            @Schema(description = "유저네임 입니다.", example = "jaeho")
            String username,
            @Schema(description = "닉네임 입니다.", example = "용사")
            String nickname,
            @Schema(description = "권한 목록입니다.", example = "{\"authority\": \"ROLE_USER\"}")
            Collection<? extends GrantedAuthority> authorities) implements AuthResponseDto {
        public Signup(User user) {
            this(user.getUsername(), user.getNickname(), List.of(new SimpleGrantedAuthority(user.getRole().name())));
        }
    }
    @Schema(description = "로그인 응답 DTO")
    record Signin(
            @Schema(description = "액세스 토큰입니다.", example = "eyJhbGciOiJIUzI1NiJ9")
            String accessToken,
            @Schema(description = "액세스 토큰입니다.", example = "eyJhbGciOiJIUzI1NiJ9")
            String refreshToken) implements AuthResponseDto { }
}
