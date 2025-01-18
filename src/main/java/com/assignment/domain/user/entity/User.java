package com.assignment.domain.user.entity;

import com.assignment.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        indexes = {
                @Index(name = "IDX_USERS_USERNAME", columnList = "username", unique = true),
                @Index(name = "IDX_USERS_NICKNAME", columnList = "nickname", unique = true)
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User(String username, String password, String nickname, UserRole role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }
}
