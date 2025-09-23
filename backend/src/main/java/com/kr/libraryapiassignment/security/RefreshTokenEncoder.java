package com.kr.libraryapiassignment.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
public class RefreshTokenEncoder {
    private final Logger logger = LoggerFactory.getLogger(RefreshTokenEncoder.class);

    public String encode(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawToken.getBytes());
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            logger.error("No such algorithm as SHA-256");
            
            throw new RuntimeException(e);
        }
    }
}
