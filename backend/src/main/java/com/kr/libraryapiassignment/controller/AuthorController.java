package com.kr.libraryapiassignment.controller;

import com.kr.libraryapiassignment.dto.author.AuthorRequestDTO;
import com.kr.libraryapiassignment.dto.author.AuthorResponseDTO;
import com.kr.libraryapiassignment.response.ApiResponse;
import com.kr.libraryapiassignment.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuthorResponseDTO>>> findAll() {
        return authorService.findAll().toEntity();
    }

    @GetMapping("/name/{lastName}")
    public ResponseEntity<ApiResponse<List<AuthorResponseDTO>>> findAllByLastName(@PathVariable String lastName) {
        return authorService.findAllByLastName(lastName).toEntity();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AuthorResponseDTO>> addAuthor(@RequestBody AuthorRequestDTO dto) {
        return authorService.save(dto).toEntity();
    }
}
