package com.kr.libraryapiassignment.mapper;

import com.kr.libraryapiassignment.dto.book.BookMinimalResponseDTO;
import com.kr.libraryapiassignment.dto.loan.LoanBaseResponseDTO;
import com.kr.libraryapiassignment.dto.loan.LoanRequestDTO;
import com.kr.libraryapiassignment.dto.loan.LoanResponseDTO;
import com.kr.libraryapiassignment.dto.loan.LoanState;
import com.kr.libraryapiassignment.dto.user.UserMinimalResponseDTO;
import com.kr.libraryapiassignment.entity.Loan;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LoanMapper {
    public LoanBaseResponseDTO toDTOBase(Loan loan) {
        LoanState state = LoanState.BORROWED;

        if (loan.getReturnedAt() != null) {
            // if return is after due, returned late else returned (in time)
            state = loan.getReturnedAt().isAfter(loan.getDueAt()) ? LoanState.RETURNED_LATE : LoanState.RETURNED;

        } else if (loan.getDueAt().isBefore(LocalDateTime.now())) {
            // if not returned and due is before now, expired.
            state = LoanState.EXPIRED;
        }

        return new LoanBaseResponseDTO(loan.getId(), state, loan.getBorrowedAt(), loan.getDueAt(),
                                       loan.getReturnedAt());
    }

    public LoanResponseDTO toDTO(Loan loan, BookMinimalResponseDTO book, UserMinimalResponseDTO user) {
        return new LoanResponseDTO(toDTOBase(loan), book, user);
    }

    public Loan toEntity(LoanRequestDTO dto) {
        LocalDateTime borrowed = LocalDateTime.now();
        LocalDateTime due = borrowed.plusDays(14);

        return new Loan(null, dto.userId(), dto.bookId(), borrowed, due, null);
    }
}
