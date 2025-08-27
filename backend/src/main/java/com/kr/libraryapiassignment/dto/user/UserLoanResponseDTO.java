package com.kr.libraryapiassignment.dto.user;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kr.libraryapiassignment.dto.book.BookMinimalResponseDTO;
import com.kr.libraryapiassignment.dto.loan.LoanBaseResponseDTO;

public record UserLoanResponseDTO(@JsonUnwrapped LoanBaseResponseDTO loan, BookMinimalResponseDTO book) {
}
