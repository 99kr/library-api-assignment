package com.kr.libraryapiassignment.dto.book;

import com.kr.libraryapiassignment.entity.Author;
import com.kr.libraryapiassignment.entity.Book;
import org.springframework.lang.NonNull;

public record BookDetailedTransientDTO(Book book, Author author) {
    @Override
    @NonNull
    public String toString() {
        return "BookDetailedTransientDTO{" +
                "book=" + book +
                ", author=" + author +
                '}';
    }
}
