package com.kr.libraryapiassignment.dto;

import org.springframework.lang.NonNull;

public record BookResponseDTO(Long id, String title, int publicationYear, int availableCopies, int totalCopies) {
    @Override
    @NonNull
    public String toString() {
        return "BookResponseDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", publicationYear=" + publicationYear +
                ", availableCopies=" + availableCopies +
                ", totalCopies=" + totalCopies +
                '}';
    }
}
