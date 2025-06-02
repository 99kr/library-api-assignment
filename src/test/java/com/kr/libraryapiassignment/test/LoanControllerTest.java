package com.kr.libraryapiassignment.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kr.libraryapiassignment.dto.loan.LoanRequestDTO;
import com.kr.libraryapiassignment.entity.Book;
import com.kr.libraryapiassignment.entity.User;
import com.kr.libraryapiassignment.mock.BookMock;
import com.kr.libraryapiassignment.mock.UserMock;
import com.kr.libraryapiassignment.repository.BookRepository;
import com.kr.libraryapiassignment.repository.LoanRepository;
import com.kr.libraryapiassignment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class LoanControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        loanRepository.deleteAll();
        userRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /loans | User & Book ID not found")
    void postLoan_NoUserOrBookId() throws Exception {
        String json = getRequestJson(1L, 1L);

        mockMvc.perform(post("/api/v1/loans").contentType(MediaType.APPLICATION_JSON).content(json))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errors.length()").value(2))
               .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("POST /loans | User ID not found")
    void postLoan_NoUserId() throws Exception {
        Book book = bookRepository.save(BookMock.entity());

        String json = getRequestJson(1L, book.getId());

        mockMvc.perform(post("/api/v1/loans").contentType(MediaType.APPLICATION_JSON).content(json))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errors.length()").value(1))
               .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("POST /loans | Book ID not found")
    void postLoan_NoBookId() throws Exception {
        User user = userRepository.save(UserMock.entity());

        String json = getRequestJson(user.getId(), 1L);

        mockMvc.perform(post("/api/v1/loans").contentType(MediaType.APPLICATION_JSON).content(json))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errors.length()").value(1))
               .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("POST /loans | No available copies")
    void postLoan_NoAvailableCopies() throws Exception {
        User user = userRepository.save(UserMock.entity());

        Book bookMock = BookMock.entity();
        bookMock.setAvailableCopies(0);

        Book book = bookRepository.save(bookMock);

        String json = getRequestJson(user.getId(), book.getId());

        mockMvc.perform(post("/api/v1/loans").contentType(MediaType.APPLICATION_JSON).content(json))
               .andExpect(status().isConflict())
               .andExpect(jsonPath("$.errors.length()").value(1))
               .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("POST /loans | Successful")
    void postLoan_Success() throws Exception {
        User user = userRepository.save(UserMock.entity());
        Book book = bookRepository.save(BookMock.entity());

        String json = getRequestJson(user.getId(), book.getId());

        mockMvc.perform(post("/api/v1/loans").contentType(MediaType.APPLICATION_JSON).content(json))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.errors.length()").value(0))
               .andExpect(jsonPath("$.data").exists())
               .andExpect(jsonPath("$.data.id").isNumber())
               .andExpect(jsonPath("$.data.state").value("BORROWED"));
    }

    private String getRequestJson(Long userId, Long bookId) {
        try {
            return objectMapper.writeValueAsString(new LoanRequestDTO(userId, bookId));
        } catch (JsonProcessingException e) {
            return "invalid";
        }
    }
}
