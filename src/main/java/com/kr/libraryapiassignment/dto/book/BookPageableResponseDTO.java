package com.kr.libraryapiassignment.dto.book;

import java.util.List;

public record BookPageableResponseDTO(
        List<BookResponseDTO> content,
        int totalPages, long totalElements,
        int numberOfElements, int size,
        boolean last, boolean first,
        boolean hasPrevious, boolean hasNext) {
}
