package com.bookstudio.shared.repository;

import com.bookstudio.shared.model.LiteraryGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiteraryGenreRepository extends JpaRepository<LiteraryGenre, Long> {
}
