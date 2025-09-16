package com.kr.libraryapiassignment.dto.book;

public record PageableResponse(
        int totalPages, long totalElements,
        int numberOfElements, int size,
        boolean last, boolean first,
        boolean hasPrevious, boolean hasNext
) {
}
