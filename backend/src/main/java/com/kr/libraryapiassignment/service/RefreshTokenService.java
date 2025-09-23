package com.kr.libraryapiassignment.service;

import com.kr.libraryapiassignment.entity.RefreshToken;
import com.kr.libraryapiassignment.repository.RefreshTokenRepository;
import com.kr.libraryapiassignment.security.RefreshTokenEncoder;
import com.kr.libraryapiassignment.security.jwt.RefreshTokenStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenEncoder refreshTokenEncoder;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, RefreshTokenEncoder refreshTokenEncoder) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenEncoder = refreshTokenEncoder;
    }

    public void saveToken(String token, int maxAgeMs) {
        Instant expiryDate = Instant.ofEpochMilli(
                Instant.now().toEpochMilli() + maxAgeMs
        );

        String hashedToken = refreshTokenEncoder.encode(token);

        refreshTokenRepository.save(new RefreshToken(hashedToken, expiryDate));
    }

    public RefreshTokenStatus getTokenStatus(String token) {
        String hashedToken = refreshTokenEncoder.encode(token);

        Optional<RefreshToken> optRefreshToken = refreshTokenRepository.getRefreshTokenByToken(hashedToken);

        if (optRefreshToken.isEmpty())
            return RefreshTokenStatus.INVALID;

        if (optRefreshToken.get().getExpiryDate().isBefore(Instant.now()))
            return RefreshTokenStatus.EXPIRED;

        return RefreshTokenStatus.VALID;
    }

    @Transactional
    public void deleteToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    @Transactional
    public int removeExpiredTokens() {
        return refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
    }
}
