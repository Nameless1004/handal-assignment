package com.assignment.domain.auth.service;

import com.assignment.domain.security.TokenUtils;
import com.assignment.domain.auth.dto.AuthRequestDto;
import com.assignment.domain.auth.dto.AuthResponseDto;
import com.assignment.domain.user.entity.User;
import com.assignment.domain.security.enums.TokenType;
import com.assignment.common.exceptions.DuplicatedException;
import com.assignment.common.exceptions.WrongPasswordException;
import com.assignment.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtils tokenUtils;

    @Override
    public AuthResponseDto.Signup signup(AuthRequestDto.Signup request) {
        String username = request.username();
        String nickname = request.nickname();
        String password = request.password();

        if(userRepository.existsByUsername(username)) {
            throw new DuplicatedException("Username is already in use");
        }

        if(userRepository.existsByNickname(nickname)) {
            throw new DuplicatedException("Nickname is already in use");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword, nickname, "ROLE_USER");
        user = userRepository.save(user);
        return new AuthResponseDto.Signup(user);
    }

    @Override
    public AuthResponseDto.Signin signin(AuthRequestDto.Signin request) {
        User find = userRepository.findByUsernameOrElseThrow(request.username());

        if(!passwordEncoder.matches(request.password(), find.getPassword())) {
            throw new WrongPasswordException();
        }

        String accessToken = tokenUtils.generateJwt(find.getId().toString(), find.getUsername(), find.getNickname(), find.getRole(), TokenType.ACCESS);
        String refreshToken = tokenUtils.generateJwt(find.getId().toString(), find.getUsername(), find.getNickname(), find.getRole(), TokenType.REFRESH);

        return new AuthResponseDto.Signin(accessToken, refreshToken);
    }
}
