package com.kr.libraryapiassignment.service;

import com.kr.libraryapiassignment.dto.book.*;
import com.kr.libraryapiassignment.entity.Book;
import com.kr.libraryapiassignment.exception.BookNotFoundException;
import com.kr.libraryapiassignment.mapper.BookMapper;
import com.kr.libraryapiassignment.repository.AuthorRepository;
import com.kr.libraryapiassignment.repository.BookRepository;
import com.kr.libraryapiassignment.response.ApiResponse;
import com.kr.libraryapiassignment.security.audit.AuditLogAction;
import com.kr.libraryapiassignment.security.audit.AuditLogger;
import com.kr.libraryapiassignment.specification.BookSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;
    private final AuditLogger auditLogger;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, BookMapper bookMapper,
                       AuditLogger auditLogger) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
        this.auditLogger = auditLogger;
    }

    public ApiResponse<BookPageableResponseDTO> findAll(BookPageableRequestDTO dto) {
        ApiResponse<BookPageableResponseDTO> response = new ApiResponse<>();

        PageRequest pageRequest = buildPageRequest(dto);
        Specification<Book> bookSpec = BookSpecification.filter(dto);

        Page<Book> pageBooks = bookRepository.findAll(bookSpec, pageRequest);

        return response.setData(bookMapper.toDTOPageable(pageBooks));
    }

    public ApiResponse<List<BookDetailedResponseDTO>> searchBooks(Optional<String> title, Optional<String> author) {
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

    public ApiResponse<BookDetailedPageableResponseDTO> findAllDetailed(BookPageableRequestDTO dto) {
        ApiResponse<BookDetailedPageableResponseDTO> response = new ApiResponse<>();

        PageRequest pageRequest = buildPageRequest(dto);
        Specification<Book> bookSpec = BookSpecification.filter(dto);

        Page<BookDetailedTransientDTO> books = bookRepository.findDetailed(bookSpec, pageRequest);

        return response.setData(bookMapper.toDTODetailedPageable(books));
    }

    public ApiResponse<BookResponseDTO> findById(Long id) throws BookNotFoundException {
        ApiResponse<BookResponseDTO> response = new ApiResponse<>();

        Optional<Book> bookOpt = bookRepository.findByIdNative(id);
        if (bookOpt.isEmpty()) throw new BookNotFoundException(id);

        return response.setData(bookMapper.toDTO(bookOpt.get()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BookResponseDTO> save(BookRequestDTO dto, Authentication authentication) {
        ApiResponse<BookResponseDTO> response = new ApiResponse<>();

        if (dto.title() == null || dto.title().isBlank())
            response.addError("title", "Missing field 'title'.");

        if (dto.authorId() == null || dto.authorId() == 0)
            response.addError("authorId", "Missing field 'authorId'.");
        else if (!authorRepository.existsById(dto.authorId()))
            response.addError("authorId", "No author exists by id '" + dto.authorId() + "'.");

        if (response.hasErrors())
            return response.setStatusCode(HttpStatus.BAD_REQUEST);

        Book book = bookRepository.save(bookMapper.toEntity(dto));

        auditLogger.log(authentication.getName(), AuditLogAction.CREATED_BOOK, "/books", "Book ID: " + book.getId());

        return response.setData(bookMapper.toDTO(book)).setStatusCode(HttpStatus.CREATED);
    }

    private PageRequest buildPageRequest(BookPageableRequestDTO dto) {
        int pageNum = dto.page().orElse(0);
        int pageSize = 20;

        Sort.Direction direction = Sort.Direction
                .fromOptionalString(dto.sortOrder().orElse("asc"))
                .orElse(Sort.Direction.ASC);

        Set<String> validSortFields = Set.of("id", "title", "publicationYear", "availableCopies", "totalCopies");
        String sortBy = dto.sortBy().filter(validSortFields::contains).orElse("id");

        Sort sort = Sort.by(direction, sortBy);

        return PageRequest.of(pageNum, pageSize, sort);
    }
}
