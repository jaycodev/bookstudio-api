package com.bookstudio.author.repository;

import com.bookstudio.author.model.Author;
import com.bookstudio.shared.enums.Status;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByStatus(Status status);
}
