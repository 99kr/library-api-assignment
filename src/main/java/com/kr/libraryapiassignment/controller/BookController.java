package com.kr.libraryapiassignment.controller;

import com.kr.libraryapiassignment.dto.book.BookDetailedResponseDTO;
import com.kr.libraryapiassignment.dto.book.BookRequestDTO;
import com.kr.libraryapiassignment.dto.book.BookResponseDTO;
import com.kr.libraryapiassignment.exception.BookNotFoundException;
import com.kr.libraryapiassignment.response.ApiResponse;
import com.kr.libraryapiassignment.service.BookService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> getAll() {
        return bookService.findAll().toEntity();
    }

    @GetMapping("/detailed")
    public ResponseEntity<ApiResponse<List<BookDetailedResponseDTO>>> getAllDetailed() {
        return bookService.findAllDetailed().toEntity();
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
        return bookService.findAll(title, author).toEntity();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponseDTO>> addBook(@RequestBody BookRequestDTO dto) {
        return bookService.save(dto).toEntity();
    }
}
