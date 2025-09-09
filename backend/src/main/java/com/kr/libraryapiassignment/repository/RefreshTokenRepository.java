package com.kr.libraryapiassignment.repository;

import com.kr.libraryapiassignment.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByToken(String token);

    Optional<RefreshToken> getRefreshTokenByToken(String token);
}
