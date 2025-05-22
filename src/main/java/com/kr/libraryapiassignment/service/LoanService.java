package com.kr.libraryapiassignment.service;

import com.kr.libraryapiassignment.dto.book.BookMinimalResponseDTO;
import com.kr.libraryapiassignment.dto.loan.LoanBaseResponseDTO;
import com.kr.libraryapiassignment.dto.loan.LoanRequestDTO;
import com.kr.libraryapiassignment.dto.loan.LoanResponseDTO;
import com.kr.libraryapiassignment.dto.user.UserMinimalResponseDTO;
import com.kr.libraryapiassignment.entity.Book;
import com.kr.libraryapiassignment.entity.Loan;
import com.kr.libraryapiassignment.entity.User;
import com.kr.libraryapiassignment.mapper.BookMapper;
import com.kr.libraryapiassignment.mapper.LoanMapper;
import com.kr.libraryapiassignment.mapper.UserMapper;
import com.kr.libraryapiassignment.repository.BookRepository;
import com.kr.libraryapiassignment.repository.LoanRepository;
import com.kr.libraryapiassignment.repository.UserRepository;
import com.kr.libraryapiassignment.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanMapper loanMapper;
    private final BookMapper bookMapper;
    private final UserMapper userMapper;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository,
                       LoanMapper loanMapper, BookMapper bookMapper, UserMapper userMapper) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.loanMapper = loanMapper;
        this.bookMapper = bookMapper;
        this.userMapper = userMapper;
    }

    @Transactional
    public ApiResponse<LoanResponseDTO> createLoan(@RequestBody LoanRequestDTO dto) {
        ApiResponse<LoanResponseDTO> response = new ApiResponse<>();

        if (dto.userId() == null || dto.userId() == 0)
            response.addError("userId", "Missing field 'userId'.");
        else if (!userRepository.existsById(dto.userId()))
            response.addError("userId", "No user found by id '" + dto.userId() + "'.");

        if (dto.bookId() == null || dto.bookId() == 0)
            response.addError("bookId", "Missing field 'bookId'.");
        else if (!bookRepository.existsById(dto.bookId()))
            response.addError("bookId", "No book found by id '" + dto.bookId() + "'.");

        if (response.hasErrors())
            return response.setStatusCode(HttpStatus.BAD_REQUEST);

        assert dto.bookId() != null;
        assert dto.userId() != null;

        Book book = bookRepository
                .findById(dto.bookId())
                .orElseThrow(
                        () -> new IllegalStateException("Book with id '" + dto.bookId() + "' expected but not found."));

        User user = userRepository
                .findById(dto.userId())
                .orElseThrow(
                        () -> new IllegalStateException(
                                "User with id '" + dto.userId() + "' expected but not found."));

        if (book.getAvailableCopies() <= 0)
            return response
                    .addError("bookId", "Book with id '" + dto.bookId() + "' has no copies available.")
                    .setStatusCode(HttpStatus.CONFLICT);

        book.setAvailableCopies(book.getAvailableCopies() - 1);

        bookRepository.save(book);
        Loan loan = loanRepository.save(loanMapper.toEntity(dto));

        BookMinimalResponseDTO bookDTO = bookMapper.toDTOMinimal(book);
        UserMinimalResponseDTO userDTO = userMapper.toDTOMinimal(user);

        return response.setData(loanMapper.toDTO(loan, bookDTO, userDTO)).setStatusCode(HttpStatus.CREATED);
    }

    @Transactional
    public ApiResponse<LoanBaseResponseDTO> returnLoan(Long loanId) {
        ApiResponse<Loan> response = findAndValidateBookReturn(loanId);
        if (response.hasErrors()) return response.cast();

        Loan loan = response.getData();

        Optional<Book> bookOpt = bookRepository.findById(loan.getBookId());

        if (bookOpt.isEmpty())
            throw new IllegalStateException("Loan with id '" + loanId + "' refers to an invalid or missing book.");

        Book book = bookOpt.get();
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        // Just warn in the console,
        // doesn't make sense to prohibit the request since you would want to be able to return the book - so it doesnt expire
        if (book.getAvailableCopies() > book.getTotalCopies())
            System.out.println("WARNING: Book with id '" + book.getId() + "' exceeded total copies.");


        loan.setReturnedAt(LocalDateTime.now());
        loanRepository.save(loan);
        bookRepository.save(book);

        return new ApiResponse<LoanBaseResponseDTO>().setData(loanMapper.toDTOBase(loan));
    }

    public ApiResponse<LoanBaseResponseDTO> extendLoan(Long loanId) {
        ApiResponse<Loan> response = findAndValidateBookReturn(loanId);

        if (response.hasErrors()) return response.cast();

        Loan loan = response.getData();

        // Unsure about the extension length, but works for now
        // maybe a check if due date is >= 28 days, which would mean it's already been extended, but probably is inconsistent
        loan.setDueAt(loan.getDueAt().plusDays(14));
        loanRepository.save(loan);

        return new ApiResponse<LoanBaseResponseDTO>().setData(loanMapper.toDTOBase(loan));
    }

    private ApiResponse<Loan> findAndValidateBookReturn(Long loanId) {
        ApiResponse<Loan> response = new ApiResponse<>();

        Optional<Loan> loanOpt = loanRepository.findById(loanId);

        if (loanOpt.isEmpty())
            return response
                    .addError("loanId", "No loan found by id '" + loanId + "'.")
                    .setStatusCode(HttpStatus.NOT_FOUND);

        Loan loan = loanOpt.get();

        if (loan.getReturnedAt() != null)
            return response
                    .addError("loanId", "Loan with id '" + loanId + "' has already been returned.")
                    .setStatusCode(HttpStatus.CONFLICT);

        return response.setData(loan);
    }
}
