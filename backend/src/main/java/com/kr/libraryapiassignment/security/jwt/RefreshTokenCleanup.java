package com.kr.libraryapiassignment.security.jwt;

import com.kr.libraryapiassignment.service.RefreshTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenCleanup implements CommandLineRunner {
    private final RefreshTokenService refreshTokenService;
    private final Logger logger = LoggerFactory.getLogger(RefreshTokenCleanup.class);

    public RefreshTokenCleanup(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void run(String... args) {
        int deleted = refreshTokenService.removeExpiredTokens();

        logger.info("Deleted {} expired refresh tokens", deleted);
    }
}
