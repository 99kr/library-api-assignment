package com.kr.libraryapiassignment.repository;

import com.kr.libraryapiassignment.dto.BookDetailedResponseDTO;
import com.kr.libraryapiassignment.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT "
            + "new com.kr.libraryapiassignment.dto.BookDetailedResponseDTO("
            + "b.id, b.title, b.publicationYear, b.availableCopies, b.totalCopies, a) "
            + "from Book b JOIN Author a ON a.id = b.authorId")
    List<BookDetailedResponseDTO> findDetailed();

    @Query("SELECT "
            + "new com.kr.libraryapiassignment.dto.BookDetailedResponseDTO("
            + "b.id, b.title, b.publicationYear, b.availableCopies, b.totalCopies, a) "
            + "from Book b JOIN Author a ON a.id = b.authorId "
            + "WHERE b.title LIKE %:title% AND a.fullName LIKE %:author%")
    List<BookDetailedResponseDTO> findDetailedByTitleAndAuthor(@Param("title") String title,
                                                               @Param("author") String author);

    // Fulfill 5. Custom Queries with native SQL and optional return type
    @Query(value = "SELECT b.* from books b WHERE b.book_id = :id", nativeQuery = true)
    Optional<Book> findByIdNative(@Param("id") Long id);
}
