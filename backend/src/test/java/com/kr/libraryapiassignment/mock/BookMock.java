package com.kr.libraryapiassignment.mock;

import com.kr.libraryapiassignment.entity.Book;

public class BookMock {
    private static Long nextId = 0L;

    public static Book entity() {
        nextId++;
        return new Book(null, "Test book " + nextId, 2025, 1, 1, 1L);
    }

    public static Book entityWithId() {
        nextId++;
        return new Book(nextId, "Test book " + nextId, 2025, 1, 1, 1L);
    }
}
