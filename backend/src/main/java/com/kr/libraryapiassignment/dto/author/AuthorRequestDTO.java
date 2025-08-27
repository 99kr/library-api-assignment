package com.kr.libraryapiassignment.dto.author;

import org.springframework.lang.NonNull;

public record AuthorRequestDTO(String firstName, String lastName, int birthYear, String nationality) {
    @Override
    @NonNull
    public String toString() {
        return "AuthorRequestDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthYear=" + birthYear +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
