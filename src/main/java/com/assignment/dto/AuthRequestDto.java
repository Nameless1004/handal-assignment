package com.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public sealed interface AuthRequestDto permits AuthRequestDto.Signup, AuthRequestDto.Signin{
    @Schema(description = "회원가입 요청 DTO")
    record Signup(
            @Schema(description = "이름 혹은 아이디", example = "jaeho")
            @NotBlank(message = "username은 필수값입니다.")
            String username,

            @Schema(description = "비밀번호", example = "abcd1234")
            @NotBlank(message = "password는 필수값입니다.")
            String password,
            
            @Schema(description = "닉네임", example = "용사")
            @NotBlank(message = "nickname은 필수값입니다.")
            String nickname
    ) implements AuthRequestDto{}
    @Schema(description = "로그인 요청 DTO")
    record Signin(
            @Schema(description = "이름 혹은 아이디", example = "jaeho")
            @NotBlank(message = "username은 필수값입니다.")
            String username,

            @Schema(description = "비밀번호", example = "abcd1234")
            @NotBlank(message = "password는 필수값입니다.")
            String password) implements AuthRequestDto{}
}
