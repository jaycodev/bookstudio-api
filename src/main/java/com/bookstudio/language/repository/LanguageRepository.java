package com.bookstudio.language.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstudio.language.model.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    List<Language> findAllByOrderByNameAsc();
}
