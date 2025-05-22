package com.kr.libraryapiassignment.repository;

import com.kr.libraryapiassignment.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
