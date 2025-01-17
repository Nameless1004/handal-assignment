package com.assignment.repository;

import com.assignment.entity.User;
import com.assignment.exceptions.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByNickname(String username);

    Optional<User> findByUsername(String username);

    default User findByUsernameOrElseThrow(String username) {
        return findByUsername(username)
                .orElseThrow(()-> new NotFoundException("User not found"));
    }
}
