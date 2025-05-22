package com.kr.libraryapiassignment.mapper;

import com.kr.libraryapiassignment.dto.author.AuthorRequestDTO;
import com.kr.libraryapiassignment.dto.author.AuthorResponseDTO;
import com.kr.libraryapiassignment.entity.Author;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorMapper {
    public AuthorResponseDTO toDTO(Author author) {
        return new AuthorResponseDTO(author.getId(), author.getFirstName(), author.getLastName(), author.getBirthYear(),
                                     author.getNationality());
    }

    public List<AuthorResponseDTO> toDTO(List<Author> authors) {
        return authors.stream().map(this::toDTO).toList();
    }

    public Author toEntity(AuthorRequestDTO dto) {
        return new Author(null, dto.firstName(), dto.lastName(), dto.birthYear(), dto.nationality());
    }
}
