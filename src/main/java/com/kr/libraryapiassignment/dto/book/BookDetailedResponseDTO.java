package com.kr.libraryapiassignment.dto.book;

import com.kr.libraryapiassignment.dto.author.AuthorResponseDTO;
import org.springframework.lang.NonNull;

public record BookDetailedResponseDTO(Long id, String title, int publicationYear, int availableCopies, int totalCopies,
                                      AuthorResponseDTO author) {
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
