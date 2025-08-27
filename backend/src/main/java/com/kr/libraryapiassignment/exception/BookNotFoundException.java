package com.kr.libraryapiassignment.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id) {
        super("No book found by id " + id);
    }
}
