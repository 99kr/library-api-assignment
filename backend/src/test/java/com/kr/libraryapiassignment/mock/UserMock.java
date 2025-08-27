package com.kr.libraryapiassignment.mock;

import com.kr.libraryapiassignment.entity.User;

import java.time.LocalDateTime;

public class UserMock {
    private static Long nextId = 0L;

    public static User entity() {
        nextId++;
        return new User(null, "Test", nextId.toString(), "test@mail.com", "password123", LocalDateTime.now());
    }

    public static User entityWithId() {
        nextId++;
        return new User(nextId, "Test", nextId.toString(), "test@mail.com", "password123", LocalDateTime.now());
    }
}
