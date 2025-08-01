package com.bookstudio.language.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bookstudio.language.dto.LanguageSummaryDto;
import com.bookstudio.language.model.Language;
import com.bookstudio.language.repository.LanguageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;

    public List<Language> getForSelect() {
        return languageRepository.findAllByOrderByNameAsc();
    }

    public Optional<Language> findById(Long languageId) {
        return languageRepository.findById(languageId);
    }

    public LanguageSummaryDto toSummaryDto(Language language) {
        return LanguageSummaryDto.builder()
                .id(language.getLanguageId())
                .name(language.getName())
                .code(language.getCode())
                .build();
    }
}
