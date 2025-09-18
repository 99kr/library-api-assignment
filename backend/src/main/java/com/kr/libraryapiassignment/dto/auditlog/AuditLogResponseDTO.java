package com.kr.libraryapiassignment.dto.auditlog;

import java.time.LocalDateTime;

public record AuditLogResponseDTO(Long id, String email, String action, String resource, String details,
                                  LocalDateTime timestamp) {
}
