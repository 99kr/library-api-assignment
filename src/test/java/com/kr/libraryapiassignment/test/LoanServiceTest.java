package com.kr.libraryapiassignment.test;

import com.kr.libraryapiassignment.dto.loan.LoanRequestDTO;
import com.kr.libraryapiassignment.dto.loan.LoanResponseDTO;
import com.kr.libraryapiassignment.dto.loan.LoanState;
import com.kr.libraryapiassignment.entity.Book;
import com.kr.libraryapiassignment.entity.Loan;
import com.kr.libraryapiassignment.entity.User;
import com.kr.libraryapiassignment.mapper.BookMapper;
import com.kr.libraryapiassignment.mapper.LoanMapper;
import com.kr.libraryapiassignment.mapper.UserMapper;
import com.kr.libraryapiassignment.mock.BookMock;
import com.kr.libraryapiassignment.mock.LoanMock;
import com.kr.libraryapiassignment.mock.UserMock;
import com.kr.libraryapiassignment.repository.BookRepository;
import com.kr.libraryapiassignment.repository.LoanRepository;
import com.kr.libraryapiassignment.repository.UserRepository;
import com.kr.libraryapiassignment.response.ApiResponse;
import com.kr.libraryapiassignment.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
public class LoanServiceTest {
    private LoanService loanService;

    @MockitoBean
    private LoanRepository loanRepository;

    @MockitoBean
    private BookRepository bookRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private LoanMapper loanMapper;

    @MockitoBean
    private BookMapper bookMapper;

    @MockitoBean
    private UserMapper userMapper;

    @BeforeEach
    public void setup() {
        loanService = new LoanService(loanRepository, bookRepository, userRepository, loanMapper, bookMapper,
                                      userMapper);
    }

    @Test
    @DisplayName("LoanService.createLoan | Successful")
    public void createLoan_Success() {
        User user = UserMock.entityWithId();
        Book book = BookMock.entityWithId();
        Loan loan = LoanMock.entityWithId(user.getId(), book.getId());

        LoanResponseDTO responseDTO = LoanMock.response(loan, book, user);

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(bookRepository.existsById(book.getId())).thenReturn(true);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        when(loanMapper.toDTO(any(), any(), any())).thenReturn(responseDTO);

        ApiResponse<LoanResponseDTO> response = loanService.createLoan(new LoanRequestDTO(user.getId(), book.getId()));
        LoanResponseDTO data = response.getData();

        // Error & Data
        assertTrue(response.getErrors().isEmpty());
        assertNotNull(data);

        // User
        assertEquals(user.getId(), data.user().id());
        assertEquals(user.getFirstName(), data.user().firstName());
        assertEquals(user.getLastName(), data.user().lastName());
        assertEquals(user.getEmail(), data.user().email());

        // Book
        assertEquals(book.getId(), data.book().id());
        assertEquals(book.getTitle(), data.book().title());
        assertEquals(0, book.getAvailableCopies());

        // Loan
        assertEquals(loan.getId(), data.loan().id());
        assertEquals(LoanState.BORROWED, data.loan().state());
        assertEquals(data.loan().borrowedAt().plusDays(14), data.loan().dueAt());
    }

    @Test
    @DisplayName("LoanService.createLoan | User & Book ID not found")
    public void createLoan_NoUserOrBookId() {
        when(userRepository.existsById(1L)).thenReturn(false);
        when(bookRepository.existsById(1L)).thenReturn(false);

        ApiResponse<LoanResponseDTO> response = loanService.createLoan(new LoanRequestDTO(1L, 1L));
        LoanResponseDTO data = response.getData();

        assertTrue(response.hasErrors());
        assertEquals(2, response.getErrors().size());
        assertNull(data);
    }

    @Test
    @DisplayName("LoanService.createLoan | User ID not found")
    public void createLoan_NoUserId() {
        when(userRepository.existsById(1L)).thenReturn(false);
        when(bookRepository.existsById(1L)).thenReturn(true);

        ApiResponse<LoanResponseDTO> response = loanService.createLoan(new LoanRequestDTO(1L, 1L));
        LoanResponseDTO data = response.getData();

        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertNull(data);
    }

    @Test
    @DisplayName("LoanService.createLoan | Book ID not found")
    public void createLoan_NoBookId() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.existsById(1L)).thenReturn(false);

        ApiResponse<LoanResponseDTO> response = loanService.createLoan(new LoanRequestDTO(1L, 1L));
        LoanResponseDTO data = response.getData();

        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertNull(data);
    }

    @Test
    @DisplayName("LoanService.createLoan | No available copies")
    public void createLoan_NoAvailableCopies() {
        User user = UserMock.entityWithId();
        Book book = BookMock.entityWithId();

        book.setAvailableCopies(0);

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(bookRepository.existsById(book.getId())).thenReturn(true);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        ApiResponse<LoanResponseDTO> response = loanService.createLoan(new LoanRequestDTO(user.getId(), book.getId()));
        LoanResponseDTO data = response.getData();

        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertNull(data);
    }

    @Test
    @DisplayName("LoanService.createLoan | IllegalStateException | User")
    public void createLoan_IllegalStateExceptionUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.existsById(1L)).thenReturn(true);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(bookRepository.findById(1L)).thenReturn(Optional.of(BookMock.entityWithId()));

        assertThrowsExactly(IllegalStateException.class, () -> loanService.createLoan(new LoanRequestDTO(1L, 1L)));
    }

    @Test
    @DisplayName("LoanService.createLoan | IllegalStateException | Book")
    public void createLoan_IllegalStateExceptionBook() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.existsById(1L)).thenReturn(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(UserMock.entityWithId()));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrowsExactly(IllegalStateException.class, () -> loanService.createLoan(new LoanRequestDTO(1L, 1L)));
    }
}
