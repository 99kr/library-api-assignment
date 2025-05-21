package com.kr.libraryapiassignment.dto;

import org.springframework.lang.NonNull;

public record BookRequestDTO(String title, int publicationYear, int availableCopies, int totalCopies,
                             Long authorId) {
    @Override
    @NonNull
    public String toString() {
        return "BookRequestDTO{" +
                "title='" + title + '\'' +
                ", publicationYear=" + publicationYear +
                ", availableCopies=" + availableCopies +
                ", totalCopies=" + totalCopies +
                ", authorId=" + authorId +
                '}';
    }
}
