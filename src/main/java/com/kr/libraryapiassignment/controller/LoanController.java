package com.kr.libraryapiassignment.controller;

import com.kr.libraryapiassignment.dto.loan.LoanBaseResponseDTO;
import com.kr.libraryapiassignment.dto.loan.LoanRequestDTO;
import com.kr.libraryapiassignment.dto.loan.LoanResponseDTO;
import com.kr.libraryapiassignment.response.ApiResponse;
import com.kr.libraryapiassignment.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LoanResponseDTO>> createLoan(@RequestBody LoanRequestDTO dto) {
        return loanService.createLoan(dto).toEntity();
    }

    @PutMapping("/{loanId}/return")
    public ResponseEntity<ApiResponse<LoanBaseResponseDTO>> returnLoan(@PathVariable Long loanId) {
        return loanService.returnLoan(loanId).toEntity();
    }

    @PutMapping("/{loanId}/extend")
    public ResponseEntity<ApiResponse<LoanBaseResponseDTO>> extendLoan(@PathVariable Long loanId) {
        return loanService.extendLoan(loanId).toEntity();
    }
}
