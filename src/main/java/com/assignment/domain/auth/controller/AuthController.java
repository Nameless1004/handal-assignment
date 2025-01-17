package com.assignment.domain.auth.controller;

import com.assignment.domain.auth.dto.AuthRequestDto;
import com.assignment.domain.auth.dto.AuthResponseDto;
import com.assignment.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "과제", description = "회원가입 및 로그인 API")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "회원가입",
            description = "회원가입 API 입니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = AuthRequestDto.Signup.class),
                            examples = @ExampleObject(
                                    name = "회원가입 요청 예시",
                                    value = "{ \"username\": \"jaeho\", \"password\": \"abcd1234\", \"nickname\": \"용사\" }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "성공", content = {
                            @Content(schema = @Schema(implementation = AuthResponseDto.Signup.class))
                    })
            }
    )
    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto.Signup> signup(@Valid @RequestBody AuthRequestDto.Signup request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(request));
    }

    @Operation(
            summary = "로그인",
            description = "로그인 API 입니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = AuthRequestDto.Signin.class),
                            examples = @ExampleObject(
                                    name = "로그인 요청 예시",
                                    value = "{ \"username\": \"jaeho\", \"password\": \"abcd1234\" }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "성공", content = {
                            @Content(schema = @Schema(implementation = AuthResponseDto.Signin.class))
                    })
            }
    )
    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDto.Signin> signin(@Valid @RequestBody AuthRequestDto.Signin request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signin(request));
    }
}
