package com.kr.libraryapiassignment.dto.loan;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kr.libraryapiassignment.dto.book.BookMinimalResponseDTO;
import com.kr.libraryapiassignment.dto.user.UserMinimalResponseDTO;

public record LoanResponseDTO(@JsonUnwrapped LoanBaseResponseDTO loan, BookMinimalResponseDTO book,
                              UserMinimalResponseDTO user) {
}
