package com.kr.libraryapiassignment.mapper;

import com.kr.libraryapiassignment.dto.book.*;
import com.kr.libraryapiassignment.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMapper {
    private final AuthorMapper authorMapper;

    public BookMapper(AuthorMapper authorMapper) {
        this.authorMapper = authorMapper;
    }

    public BookResponseDTO toDTO(Book book) {
        return new BookResponseDTO(book.getId(), book.getTitle(), book.getPublicationYear(), book.getAvailableCopies(),
                                   book.getTotalCopies());
    }

    public List<BookResponseDTO> toDTO(List<Book> books) {
        return books.stream().map(this::toDTO).toList();
    }

    public BookDetailedResponseDTO toDTODetailed(BookDetailedTransientDTO dto) {
        Book book = dto.book();

        return new BookDetailedResponseDTO(book.getId(), book.getTitle(), book.getPublicationYear(),
                                           book.getAvailableCopies(), book.getTotalCopies(),
                                           authorMapper.toDTO(dto.author()));
    }

    public List<BookDetailedResponseDTO> toDTODetailed(List<BookDetailedTransientDTO> dtos) {
        return dtos.stream().map(this::toDTODetailed).toList();
    }

    public BookMinimalResponseDTO toDTOMinimal(Book book) {
        return new BookMinimalResponseDTO(book.getId(), book.getTitle());
    }

    public BookPageableResponseDTO toDTOPageable(Page<Book> page) {
        return new BookPageableResponseDTO(
                toDTO(page.getContent()),
                new PageableResponse(page.getTotalPages(), page.getTotalElements(),
                                     page.getNumberOfElements(), page.getSize(),
                                     page.isLast(), page.isFirst(),
                                     page.hasPrevious(), page.hasNext()));
    }

    public BookDetailedPageableResponseDTO toDTODetailedPageable(Page<BookDetailedTransientDTO> page) {
        return new BookDetailedPageableResponseDTO(
                toDTODetailed(page.getContent()),
                new PageableResponse(page.getTotalPages(), page.getTotalElements(),
                                     page.getNumberOfElements(), page.getSize(),
                                     page.isLast(), page.isFirst(),
                                     page.hasPrevious(), page.hasNext()));
    }

    public Book toEntity(BookRequestDTO dto) {
        return new Book(null, dto.title(), dto.publicationYear(), dto.availableCopies(), dto.totalCopies(),
                        dto.authorId());
    }
}