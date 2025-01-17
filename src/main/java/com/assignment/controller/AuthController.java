package com.assignment.controller;

import com.assignment.dto.AuthRequestDto;
import com.assignment.dto.AuthResponseDto;
import com.assignment.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "과제", description = "회원가입 및 로그인 API")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "회원가입 API 입니다.")
    @PostMapping("/signup")
    @ApiResponse(responseCode = "201", description = "성공", content = {
            @Content(schema = @Schema(implementation = AuthResponseDto.Signup.class))})
    public ResponseEntity<AuthResponseDto.Signup> signup(@Valid @RequestBody AuthRequestDto.Signup request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(request));
    }

    @Operation(summary = "로그인", description = "로그인 API 입니다.")
    @PostMapping("/signin")
    @ApiResponse(responseCode = "201", description = "성공", content = {
            @Content(schema = @Schema(implementation = AuthResponseDto.Signin.class))})
    public ResponseEntity<AuthResponseDto.Signin> signin(@Valid @RequestBody AuthRequestDto.Signin request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signin(request));
    }
}
