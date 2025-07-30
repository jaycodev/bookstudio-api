package com.bookstudio.shared.repository;

import com.bookstudio.shared.model.Language;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    List<Language> findAllByOrderByNameAsc();
}
