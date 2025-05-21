package com.kr.libraryapiassignment.repository;

import com.kr.libraryapiassignment.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
