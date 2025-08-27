package com.kr.libraryapiassignment.repository;

import com.kr.libraryapiassignment.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findAllByLastNameIgnoreCase(String lastName);
}
