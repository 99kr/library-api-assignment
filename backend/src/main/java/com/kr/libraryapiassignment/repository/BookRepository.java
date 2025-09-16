package com.kr.libraryapiassignment.repository;

import com.kr.libraryapiassignment.dto.book.BookDetailedTransientDTO;
import com.kr.libraryapiassignment.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query("""
            SELECT new com.kr.libraryapiassignment.dto.book.BookDetailedTransientDTO(b, a)
            FROM Book b JOIN Author a on a.id = b.authorId
            """)
    Page<BookDetailedTransientDTO> findDetailed(Specification<Book> spec, Pageable pageable);

    @Query("""
            SELECT new com.kr.libraryapiassignment.dto.book.BookDetailedTransientDTO(b, a)
            FROM Book b JOIN Author a on a.id = b.authorId
            WHERE b.title LIKE %:title% AND a.fullName LIKE %:author%
            """)
    List<BookDetailedTransientDTO> findDetailedByTitleAndAuthor(@Param("title") String title,
                                                                @Param("author") String author);

    // Fulfill 5. Custom Queries with native SQL and optional return type
    @Query(value = "SELECT b.* from books b WHERE b.book_id = :id", nativeQuery = true)
    Optional<Book> findByIdNative(@Param("id") Long id);
}
