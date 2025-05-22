package com.kr.libraryapiassignment.service;

import com.kr.libraryapiassignment.dto.book.BookDetailedResponseDTO;
import com.kr.libraryapiassignment.dto.book.BookDetailedTransientDTO;
import com.kr.libraryapiassignment.dto.book.BookRequestDTO;
import com.kr.libraryapiassignment.dto.book.BookResponseDTO;
import com.kr.libraryapiassignment.entity.Book;
import com.kr.libraryapiassignment.exception.BookNotFoundException;
import com.kr.libraryapiassignment.mapper.BookMapper;
import com.kr.libraryapiassignment.repository.AuthorRepository;
import com.kr.libraryapiassignment.repository.BookRepository;
import com.kr.libraryapiassignment.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
    }

    public ApiResponse<List<BookResponseDTO>> findAll() {
        ApiResponse<List<BookResponseDTO>> response = new ApiResponse<>();

        return response.setData(bookMapper.toDTO(bookRepository.findAll()));
    }

    public ApiResponse<List<BookDetailedResponseDTO>> findAll(Optional<String> title, Optional<String> author) {
        ApiResponse<List<BookDetailedResponseDTO>> response = new ApiResponse<>();

        String titleValue = title.orElse("");
        String authorValue = title.orElse("");

        if (titleValue.isBlank() && authorValue.isBlank()) {
            return response
                    .addError("Either 'title' or 'author' must be provided.")
                    .setStatusCode(HttpStatus.BAD_REQUEST);
        }

        List<BookDetailedTransientDTO> books = bookRepository.findDetailedByTitleAndAuthor(title.orElse(""),
                                                                                           author.orElse(""));

        return response.setData(bookMapper.toDTODetailed(books));
    }

    public ApiResponse<List<BookDetailedResponseDTO>> findAllDetailed() {
        ApiResponse<List<BookDetailedResponseDTO>> response = new ApiResponse<>();

        List<BookDetailedTransientDTO> books = bookRepository.findDetailed();

        return response.setData(bookMapper.toDTODetailed(books));
    }

    public ApiResponse<BookResponseDTO> findById(Long id) throws BookNotFoundException {
        ApiResponse<BookResponseDTO> response = new ApiResponse<>();

        Optional<Book> bookOpt = bookRepository.findByIdNative(id);
        if (bookOpt.isEmpty()) throw new BookNotFoundException(id);

        return response.setData(bookMapper.toDTO(bookOpt.get()));
    }

    public ApiResponse<BookResponseDTO> save(BookRequestDTO dto) {
        ApiResponse<BookResponseDTO> response = new ApiResponse<>();

        if (dto.title() == null || dto.title().isBlank())
            response.addError("title", "Missing field 'title'.");

        if (dto.authorId() == null || dto.authorId() == 0)
            response.addError("authorId", "Missing field 'authorId'.");
        else if (!authorRepository.existsById(dto.authorId()))
            response.addError("authorId", "No author exists by id '" + dto.authorId() + "'.");

        if (response.errorCount() > 0)
            return response.setStatusCode(HttpStatus.BAD_REQUEST);

        Book book = bookRepository.save(bookMapper.toEntity(dto));

        return response.setData(bookMapper.toDTO(book)).setStatusCode(HttpStatus.CREATED);
    }
}
