package com.kr.libraryapiassignment.mock;

import com.kr.libraryapiassignment.entity.User;

import java.time.LocalDateTime;
import java.util.Set;

public class UserMock {
    private static Long nextId = 0L;

    public static User entity() {
        nextId++;
        return new User(null, "Test", nextId.toString(), "test@mail.com", "password123", LocalDateTime.now(), Set.of());
    }

    public static User entityWithId() {
        nextId++;
        return new User(nextId, "Test", nextId.toString(), "test@mail.com", "password123", LocalDateTime.now(),
                        Set.of());
    }
}
