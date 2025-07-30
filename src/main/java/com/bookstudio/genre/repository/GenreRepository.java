package com.bookstudio.genre.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstudio.genre.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findAllByOrderByNameAsc();
}
