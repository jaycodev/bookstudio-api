package com.bookstudio.language.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstudio.language.dto.LanguageOptionDto;
import com.bookstudio.language.model.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    @Query("""
        SELECT 
            new com.bookstudio.language.dto.LanguageOptionDto(
                l.languageId, 
                l.name
            )
        FROM Language l 
        ORDER BY l.name ASC
    """)
    List<LanguageOptionDto> findForOptions();
}
