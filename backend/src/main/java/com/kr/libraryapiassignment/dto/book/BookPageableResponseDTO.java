package com.kr.libraryapiassignment.dto.book;

import java.util.List;

public record BookPageableResponseDTO(List<BookResponseDTO> content, PageableResponse pageable) {
}
