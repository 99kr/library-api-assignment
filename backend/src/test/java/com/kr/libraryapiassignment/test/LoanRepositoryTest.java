package com.kr.libraryapiassignment.test;

import com.kr.libraryapiassignment.entity.Loan;
import com.kr.libraryapiassignment.mock.LoanMock;
import com.kr.libraryapiassignment.repository.LoanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {
    @Autowired
    private LoanRepository loanRepository;

    @Test
    @DisplayName("LoanRepository.save")
    public void save() {
        Loan loan = LoanMock.entity();

        Loan savedLoan = loanRepository.save(loan);

        assertInstanceOf(Long.class, savedLoan.getId());
        assertEquals(1, savedLoan.getUserId());
        assertEquals(1, savedLoan.getBookId());
        assertEquals(loan.getBorrowedAt(), savedLoan.getBorrowedAt());
        assertEquals(loan.getDueAt(), savedLoan.getDueAt());
        assertEquals(loan.getReturnedAt(), savedLoan.getReturnedAt());
    }

    @Test
    @DisplayName("LoanRepository.findById")
    public void findById() {
        Loan loanMock = LoanMock.entity();
        Long id = loanRepository.save(loanMock).getId();

        Optional<Loan> loan = loanRepository.findById(id);

        assertDoesNotThrow(loan::get);
        assertTrue(loan.isPresent());

        assertEquals(id, loan.get().getId());
        assertEquals(1, loan.get().getUserId());
        assertEquals(1, loan.get().getBookId());
        assertEquals(loanMock.getBorrowedAt(), loan.get().getBorrowedAt());
        assertEquals(loanMock.getDueAt(), loan.get().getDueAt());
        assertEquals(loanMock.getReturnedAt(), loan.get().getReturnedAt());
    }

    @Test
    @DisplayName("LoanRepository.findAll")
    public void findAll() {
        int size = 5;

        List<Loan> loanMocks = IntStream.range(0, size).mapToObj(i -> LoanMock.entity()).toList();
        loanRepository.saveAll(loanMocks);

        List<Loan> loans = loanRepository.findAll();

        assertEquals(size, loans.size());
    }
}
