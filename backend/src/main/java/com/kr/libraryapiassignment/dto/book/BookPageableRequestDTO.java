package com.kr.libraryapiassignment.dto.book;

import java.util.Optional;

public record BookPageableRequestDTO(
        Optional<Integer> page,
        Optional<String> sortOrder,
        Optional<String> sortBy,
        Optional<Boolean> available,
        Optional<Integer> yearFrom,
        Optional<Integer> yearTo) {
}
