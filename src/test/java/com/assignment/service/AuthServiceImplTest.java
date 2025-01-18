package com.assignment.service;

import com.assignment.common.enums.ErrorMessage;
import com.assignment.domain.security.TokenUtils;
import com.assignment.domain.auth.dto.AuthRequestDto;
import com.assignment.domain.auth.dto.AuthResponseDto;
import com.assignment.domain.auth.service.AuthServiceImpl;
import com.assignment.domain.user.entity.User;
import com.assignment.common.exceptions.DuplicatedException;
import com.assignment.common.exceptions.WrongPasswordException;
import com.assignment.domain.user.enums.UserRole;
import com.assignment.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenUtils tokenUtils;

    @InjectMocks
    private AuthServiceImpl authService;

    private AuthRequestDto.Signup signupRequestDto;
    private AuthRequestDto.Signin signinRequestDto;
    private User user;

    @BeforeEach
    void setUp() {
        signupRequestDto = new AuthRequestDto.Signup("username", "password", "nickname");
        signinRequestDto = new AuthRequestDto.Signin("username", "password");
        user = new User("username", "encodedPassword", "nickname", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(user, "id", 1L);
    }

    @Test
    void 회원가입_성공() {
        // given
        given(userRepository.existsByUsername("username")).willReturn(false);
        given(userRepository.existsByNickname("nickname")).willReturn(false);
        given(passwordEncoder.encode("password")).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        AuthResponseDto.Signup response = authService.signup(signupRequestDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo("username");
        assertThat(response.nickname()).isEqualTo("nickname");
    }

    @Test
    void 회원가입_시_중복된_유저네임_예외발생() {
        // given
        given(userRepository.existsByUsername("username")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> authService.signup(signupRequestDto))
                .isInstanceOf(DuplicatedException.class)
                .hasMessage(ErrorMessage.USERNAME_DUPLICATED_MESSAGE.get());
    }

    @Test
    void 회원가입_시_중복된_닉네임_예외발생() {
        // given
        given(userRepository.existsByUsername("username")).willReturn(false);
        given(userRepository.existsByNickname("nickname")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> authService.signup(signupRequestDto))
                .isInstanceOf(DuplicatedException.class)
                .hasMessage(ErrorMessage.USERNAME_DUPLICATED_MESSAGE.get());
    }

    @Test
    void 로그인_성공() {
        // given
        given(userRepository.findByUsernameOrElseThrow("username")).willReturn(user);
        given(passwordEncoder.matches("password", "encodedPassword")).willReturn(true);
        given(tokenUtils.generateJwt(any(), any(), any(), any(), any()))
                .willReturn("accessToken", "refreshToken");

        // when
        AuthResponseDto.Signin response = authService.signin(signinRequestDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("accessToken");
        assertThat(response.refreshToken()).isEqualTo("refreshToken");
    }

    @Test
    void 로그인_시_잘못된_비밀번호_예외발생() {
        // given
        given(userRepository.findByUsernameOrElseThrow("username")).willReturn(user);
        given(passwordEncoder.matches("password", "encodedPassword")).willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.signin(signinRequestDto))
                .isInstanceOf(WrongPasswordException.class)
                .hasMessage(ErrorMessage.WRONG_PASSWORD.get());
    }
}
