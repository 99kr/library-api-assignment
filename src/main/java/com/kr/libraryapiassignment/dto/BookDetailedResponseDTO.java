package com.kr.libraryapiassignment.dto;

import com.kr.libraryapiassignment.entity.Author;
import org.springframework.lang.NonNull;

public record BookDetailedResponseDTO(Long id, String title, int publicationYear, int availableCopies, int totalCopies,
                                      Author author) {
    @Override
    @NonNull
    public String toString() {
        return "BookDetailedResponseDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", publicationYear=" + publicationYear +
                ", availableCopies=" + availableCopies +
                ", totalCopies=" + totalCopies +
                ", author=" + author +
                '}';
    }
}
