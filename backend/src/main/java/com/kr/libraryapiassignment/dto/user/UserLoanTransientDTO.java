package com.kr.libraryapiassignment.dto.user;

import com.kr.libraryapiassignment.entity.Book;
import com.kr.libraryapiassignment.entity.Loan;

public record UserLoanTransientDTO(Loan loan, Book book) {
}
