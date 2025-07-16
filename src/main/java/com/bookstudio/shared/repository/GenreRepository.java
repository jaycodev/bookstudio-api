package com.bookstudio.shared.repository;

import com.bookstudio.shared.model.Genre;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findAllByOrderByNameAsc();
}
