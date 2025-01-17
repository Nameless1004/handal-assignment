package com.assignment.controller;

import com.assignment.config.WebSecurityConfig;
import com.assignment.dto.AuthRequestDto;
import com.assignment.dto.AuthResponseDto;
import com.assignment.filter.TokenFilter;
import com.assignment.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AuthController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TokenFilter.class)
        })
@WithMockUser
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    void 회원가입_성공() throws Exception {
        // given
        AuthRequestDto.Signup request = new AuthRequestDto.Signup("test@test.com", "password123!", "테스트");
        AuthResponseDto.Signup response = new AuthResponseDto.Signup(
                "test@test.com",
                "테스트",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        given(authService.signup(any())).willReturn(response);

        // when & then
        mockMvc.perform(post("/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("test@test.com"))
                .andExpect(jsonPath("$.nickname").value("테스트"));
    }

    @Test
    void 회원가입_실패_유효성_검사() throws Exception {
        // given
        AuthRequestDto.Signup request = new AuthRequestDto.Signup("", "password", "nickname");

        // when & then
        mockMvc.perform(post("/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void 로그인_성공() throws Exception {
        // given
        AuthRequestDto.Signin request = new AuthRequestDto.Signin("username", "password");
        AuthResponseDto.Signin response = new AuthResponseDto.Signin("accessToken", "refreshToken");
        given(authService.signin(any())).willReturn(response);

        // when & then
        mockMvc.perform(post("/auth/signin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
    }

    @Test
    void 로그인_실패_유효성_검사() throws Exception {
        // given
        AuthRequestDto.Signin request = new AuthRequestDto.Signin("", "password");

        // when & then
        mockMvc.perform(post("/auth/signin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}