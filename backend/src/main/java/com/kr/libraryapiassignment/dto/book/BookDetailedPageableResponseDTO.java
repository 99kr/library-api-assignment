package com.kr.libraryapiassignment.dto.book;

import java.util.List;

public record BookDetailedPageableResponseDTO(List<BookDetailedResponseDTO> content, PageableResponse pageable) {
}
