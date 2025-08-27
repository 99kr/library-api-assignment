package com.kr.libraryapiassignment.dto.loan;

import java.time.LocalDateTime;

public record LoanBaseResponseDTO(Long id, LoanState state, LocalDateTime borrowedAt, LocalDateTime dueAt,
                                  LocalDateTime returnedAt) {
}
