package com.kr.libraryapiassignment.dto.author;

import org.springframework.lang.NonNull;

public record AuthorResponseDTO(Long id, String firstName, String lastName, int birthYear, String nationality) {
    @Override
    @NonNull
    public String toString() {
        return "AuthorResponseDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthYear=" + birthYear +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
