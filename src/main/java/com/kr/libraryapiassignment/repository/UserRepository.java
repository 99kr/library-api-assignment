package com.kr.libraryapiassignment.repository;

import com.kr.libraryapiassignment.dto.user.UserLoanTransientDTO;
import com.kr.libraryapiassignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    @Query("SELECT "
            + "new com.kr.libraryapiassignment.dto.user.UserLoanTransientDTO(l, b) "
            + "from Loan l JOIN Book b ON b.id = l.bookId "
            + "WHERE l.userId = :userId")
    List<UserLoanTransientDTO> findUserLoansById(@Param("userId") Long userId);
}
