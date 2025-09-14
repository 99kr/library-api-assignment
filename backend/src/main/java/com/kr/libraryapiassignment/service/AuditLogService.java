package com.kr.libraryapiassignment.service;

import com.kr.libraryapiassignment.dto.auditlog.AuditLogResponseDTO;
import com.kr.libraryapiassignment.entity.AuditLog;
import com.kr.libraryapiassignment.mapper.AuditLogMapper;
import com.kr.libraryapiassignment.repository.AuditLogRepository;
import com.kr.libraryapiassignment.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    public AuditLogService(AuditLogRepository auditLogRepository, AuditLogMapper auditLogMapper) {
        this.auditLogRepository = auditLogRepository;
        this.auditLogMapper = auditLogMapper;
    }

    public ApiResponse<List<AuditLogResponseDTO>> findAll() {
        ApiResponse<List<AuditLogResponseDTO>> response = new ApiResponse<>();

        List<AuditLog> auditLogs = auditLogRepository.findAll();

        response.setData(auditLogMapper.toDTO(auditLogs));
        
        return response;
    }
}
