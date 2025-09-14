package com.kr.libraryapiassignment.security.audit;

import com.kr.libraryapiassignment.entity.AuditLog;
import com.kr.libraryapiassignment.repository.AuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuditLogger {
    private final AuditLogRepository auditLogRepository;
    private final Logger logger = LoggerFactory.getLogger(AuditLogger.class);

    public AuditLogger(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String email, AuditLogAction action, String resource) {
        auditLogRepository.save(new AuditLog(email, action.toString(), resource));
        logger.info("Saved audit log ({}, {}, {})", email, action, resource);
    }
}
