package com.kr.libraryapiassignment.mapper;

import com.kr.libraryapiassignment.dto.BookRequestDTO;
import com.kr.libraryapiassignment.dto.BookResponseDTO;
import com.kr.libraryapiassignment.entity.Book;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMapper {
    public BookResponseDTO toDTO(Book book) {
        return new BookResponseDTO(book.getId(), book.getTitle(), book.getPublicationYear(), book.getAvailableCopies(),
                                   book.getTotalCopies());
    }

    public List<BookResponseDTO> toDTOList(List<Book> books) {
        return books.stream().map(this::toDTO).toList();
    }

    public Book toEntity(BookRequestDTO dto) {
        return new Book(null, dto.title(), dto.publicationYear(), dto.availableCopies(), dto.totalCopies(),
                        dto.authorId());
    }
}
