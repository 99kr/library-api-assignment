package com.kr.libraryapiassignment.dto.book;

import java.util.List;

public record BookPageableResponseDTO(int totalPages, long totalElements, List<BookResponseDTO> books) {
}
