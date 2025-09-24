package com.kr.libraryapiassignment.service;

import com.kr.libraryapiassignment.security.audit.AuditLogAction;
import com.kr.libraryapiassignment.security.audit.AuditLogger;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class BruteForceService {
    private final AuditLogger auditLogger;

    private static final int MAX_ATTEMPTS = 5;
    private static final Long LOCK_TIME = TimeUnit.MINUTES.toMillis(1);

    private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final Map<String, Long> lockCache = new ConcurrentHashMap<>();

    public BruteForceService(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
    }

    public void loginSucceeded(String key) {
        attemptsCache.remove(key);
        lockCache.remove(key); // probably not needed
    }

    public void loginFailed(String key) {
        int attempts = attemptsCache.getOrDefault(key, 0) + 1;

        // If we have reached max attempts
        if (attempts >= MAX_ATTEMPTS) {
            attemptsCache.remove(key);
            lockCache.put(key, System.currentTimeMillis());

            auditLogger.log(key, AuditLogAction.LOGIN_BLOCKED, "/auth/login", "Too many attempts");
        } else {
            attemptsCache.put(key, attempts);
        }
    }

    public Optional<Long> getLockedTime(String key) {
        if (!lockCache.containsKey(key)) return Optional.empty();

        long lockedAt = lockCache.get(key);
        long lockedFor = System.currentTimeMillis() - lockedAt;

        // Are we no longer blocked?
        if (lockedFor > LOCK_TIME) {
            lockCache.remove(key);
            return Optional.empty();
        }

        return Optional.of(LOCK_TIME - lockedFor);
    }
}
