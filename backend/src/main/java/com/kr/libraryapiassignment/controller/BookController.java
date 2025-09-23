package com.kr.libraryapiassignment.controller;

import com.kr.libraryapiassignment.dto.book.*;
import com.kr.libraryapiassignment.exception.BookNotFoundException;
import com.kr.libraryapiassignment.response.ApiResponse;
import com.kr.libraryapiassignment.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // GET /api/v1/books?page=0&sortOrder=desc&sortBy=title&available=true&yearFrom=1990&yearTo=1997
    @GetMapping
    public ResponseEntity<ApiResponse<BookPageableResponseDTO>> getAll(BookPageableRequestDTO dto) {
        return bookService.findAll(dto).toEntity();
    }

    @GetMapping("/detailed")
    public ResponseEntity<ApiResponse<BookDetailedPageableResponseDTO>> getAllDetailed(BookPageableRequestDTO dto) {
        return bookService.findAllDetailed(dto).toEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDTO>> getById(@PathVariable Long id) {
        ApiResponse<BookResponseDTO> response = new ApiResponse<>();

        try {
            response.setData(bookService.findById(id).getData());
        } catch (BookNotFoundException e) {
            response.addError("id", e.getMessage());
        }

        return response.toEntity();
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BookDetailedResponseDTO>>> searchBooks(
            @RequestParam Optional<String> title,
            @RequestParam Optional<String> author) {
        return bookService.searchBooks(title, author).toEntity();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponseDTO>> addBook(@RequestBody BookRequestDTO dto,
                                                                Authentication authentication) {
        return bookService.save(dto, authentication).toEntity();
    }
}
