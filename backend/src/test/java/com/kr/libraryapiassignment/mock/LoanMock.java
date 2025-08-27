package com.kr.libraryapiassignment.mock;

import com.kr.libraryapiassignment.dto.book.BookMinimalResponseDTO;
import com.kr.libraryapiassignment.dto.loan.LoanBaseResponseDTO;
import com.kr.libraryapiassignment.dto.loan.LoanResponseDTO;
import com.kr.libraryapiassignment.dto.loan.LoanState;
import com.kr.libraryapiassignment.dto.user.UserMinimalResponseDTO;
import com.kr.libraryapiassignment.entity.Book;
import com.kr.libraryapiassignment.entity.Loan;
import com.kr.libraryapiassignment.entity.User;

import java.time.LocalDateTime;

public class LoanMock {
    private static Long nextId = 0L;

    public static Loan entity(Long userId, Long bookId) {
        nextId++;
        LocalDateTime now = LocalDateTime.now();

        return new Loan(null, userId, bookId, now, now.plusDays(14), null);
    }

    public static Loan entityWithId(Long userId, Long bookId) {
        nextId++;
        LocalDateTime now = LocalDateTime.now();

        return new Loan(nextId, userId, bookId, now, now.plusDays(14), null);
    }

    public static Loan entity() {
        return LoanMock.entity(1L, 1L);
    }

    public static LoanResponseDTO response(Loan loan, Book book, User user) {
        return new LoanResponseDTO(
                new LoanBaseResponseDTO(loan.getId(), LoanState.BORROWED, loan.getBorrowedAt(), loan.getDueAt(),
                                        loan.getReturnedAt()),
                new BookMinimalResponseDTO(book.getId(), book.getTitle()),
                new UserMinimalResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()));
    }
}
