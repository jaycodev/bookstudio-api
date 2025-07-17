package com.bookstudio.shared.repository;

import com.bookstudio.shared.model.LiteraryGenre;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LiteraryGenreRepository extends JpaRepository<LiteraryGenre, Long> {
    List<LiteraryGenre> findAllByOrderByNameAsc();
}
