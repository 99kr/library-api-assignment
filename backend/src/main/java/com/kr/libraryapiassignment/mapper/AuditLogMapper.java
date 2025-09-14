package com.kr.libraryapiassignment.mapper;

import com.kr.libraryapiassignment.dto.auditlog.AuditLogResponseDTO;
import com.kr.libraryapiassignment.entity.AuditLog;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuditLogMapper {
    public AuditLogResponseDTO toDTO(AuditLog auditLog) {
        return new AuditLogResponseDTO(auditLog.getId(), auditLog.getEmail(), auditLog.getAction(),
                                       auditLog.getResource(),
                                       auditLog.getTimestamp());
    }

    public List<AuditLogResponseDTO> toDTO(List<AuditLog> auditLogs) {
        return auditLogs.stream().map(this::toDTO).toList();
    }
}
