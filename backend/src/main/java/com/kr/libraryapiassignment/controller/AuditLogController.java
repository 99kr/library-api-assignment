package com.kr.libraryapiassignment.controller;

import com.kr.libraryapiassignment.dto.auditlog.AuditLogResponseDTO;
import com.kr.libraryapiassignment.response.ApiResponse;
import com.kr.libraryapiassignment.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
public class AuditLogController {
    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AuditLogResponseDTO>>> findAll() {
        return auditLogService.findAll().toEntity();
    }
}
