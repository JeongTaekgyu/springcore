package com.taek.springcore.repository;


import com.taek.springcore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Optional은 값이 없다면 null로 해줘라 있다면 있는 값으로
    Optional<User> findByUsername(String username);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByEmail(String email);
}