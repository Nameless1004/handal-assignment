package com.assignment.domain.security;

import com.assignment.domain.security.enums.TokenType;
import com.assignment.domain.user.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class TokenUtils {

    private final SecretKey secretKey;

    public TokenUtils(@Value("${jwt.secret.key}") String JWT_SECRET_KEY) {
        secretKey = new SecretKeySpec(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("id", String.class);
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public String getNickname(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("nickname", String.class);
    }

    public String getType(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("type", String.class);
    }

    public String generateJwt(String id, String username, String nickname, UserRole role, TokenType tokenType) {
        return Jwts.builder()
                .claim("id", id)
                .claim("username", username)
                .claim("nickname", nickname)
                .claim("role", role.name())
                .claim("type", tokenType.name())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenType.getLifeTimeMs()))
                .signWith(secretKey)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);  // 만료된 토큰이라면 false
        } catch (ExpiredJwtException e) {
            return false;
        } catch (SignatureException e) {
            return false;
        } catch (MalformedJwtException e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration().before(new java.util.Date());
    }

}
