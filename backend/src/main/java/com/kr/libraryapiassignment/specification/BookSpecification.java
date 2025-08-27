package com.kr.libraryapiassignment.specification;

import com.kr.libraryapiassignment.dto.book.BookPageableRequestDTO;
import com.kr.libraryapiassignment.entity.Book;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BookSpecification {
    public static Specification<Book> filter(BookPageableRequestDTO dto) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dto.yearFrom().isPresent()) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("publicationYear"), dto.yearFrom().get()));
            }

            if (dto.yearTo().isPresent()) {
                predicates.add(builder.lessThanOrEqualTo(root.get("publicationYear"), dto.yearTo().get()));
            }

            if (dto.available().isPresent()) {
                if (dto.available().get()) {
                    predicates.add(builder.greaterThan(root.get("availableCopies"), 0));
                } else {
                    predicates.add(builder.equal(root.get("availableCopies"), 0));
                }
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
