package com.assignment.domain.auth.controller;

import com.assignment.domain.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "테스트", description = "현재 로그인한 유저의 정보")
public class TestController {

    @GetMapping("/test/whoami")
    @Operation(summary = "현재 로그인한 유저의 정보", description = "현재 로그인한 유저의 정보를 가져오는 API입니다.")
    public ResponseEntity<AuthUser> whoami(@AuthenticationPrincipal AuthUser user) {
        return ResponseEntity.ok(user);
    }
}
