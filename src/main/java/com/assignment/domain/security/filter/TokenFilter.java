package com.assignment.domain.security.filter;

import com.assignment.domain.security.TokenUtils;
import com.assignment.domain.security.AuthUser;
import com.assignment.domain.security.JwtAuthenticationToken;
import io.jsonwebtoken.lang.Strings;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "Auth")
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();
        String method = request.getMethod();

        log.debug(":::Request URI::: [ {}:{} ]", method, uri);

        String headerToken = request.getHeader(AUTHORIZATION_HEADER);

        // 토큰이 없거나 "Bearer "로 시작하지 않는다면 필터 건너뜀
        if(!Strings.hasText(headerToken) || !headerToken.startsWith(BEARER_PREFIX)) {

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token not found");
            return;
        }


        // prefix 제거
        String token = headerToken.substring(7);

        if(!tokenUtils.validateToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalid");
            return;
        }

        String id = tokenUtils.getId(token);
        String username = tokenUtils.getUsername(token);
        String role = tokenUtils.getRole(token);
        String nickname = tokenUtils.getNickname(token);

        if(Strings.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 인증 객체 생성
            setAuthentication(id, username, nickname, role, request);
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String id, String username, String nickname, String role, HttpServletRequest request) {
        AuthUser authUser = new AuthUser(id, username, nickname, role);
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(authUser);
        jwtAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
    }
}
