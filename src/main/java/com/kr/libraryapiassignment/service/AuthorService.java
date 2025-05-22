package com.kr.libraryapiassignment.service;

import com.kr.libraryapiassignment.dto.author.AuthorRequestDTO;
import com.kr.libraryapiassignment.dto.author.AuthorResponseDTO;
import com.kr.libraryapiassignment.entity.Author;
import com.kr.libraryapiassignment.mapper.AuthorMapper;
import com.kr.libraryapiassignment.repository.AuthorRepository;
import com.kr.libraryapiassignment.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    public ApiResponse<List<AuthorResponseDTO>> findAll() {
        ApiResponse<List<AuthorResponseDTO>> response = new ApiResponse<>();

        List<Author> authors = authorRepository.findAll();

        return response.setData(authorMapper.toDTO(authors));
    }

    public ApiResponse<List<AuthorResponseDTO>> findAllByLastName(String lastName) {
        ApiResponse<List<AuthorResponseDTO>> response = new ApiResponse<>();

        List<Author> authors = authorRepository.findAllByLastNameIgnoreCase(lastName);

        return response.setData(authorMapper.toDTO(authors));
    }

    public ApiResponse<AuthorResponseDTO> save(AuthorRequestDTO dto) {
        ApiResponse<AuthorResponseDTO> response = new ApiResponse<>();

        if (dto.firstName() == null || dto.firstName().isBlank())
            response.addError("firstName", "Missing field 'firstName'.");

        if (dto.lastName() == null || dto.lastName().isBlank())
            response.addError("lastName", "Missing field 'lastName'.");

        if (dto.birthYear() == 0)
            response.addError("birthYear", "Missing field 'birthYear'.");
        else if (dto.birthYear() < 0)
            response.addError("birthYear", "Invalid field 'birthYear'");

        if (dto.nationality() == null || dto.nationality().isBlank())
            response.addError("nationality", "Missing field 'nationality'.");

        if (response.errorCount() > 0)
            return response.setStatusCode(HttpStatus.BAD_REQUEST);

        Author author = authorRepository.save(authorMapper.toEntity(dto));

        return response.setData(authorMapper.toDTO(author)).setStatusCode(HttpStatus.CREATED);
    }
}
