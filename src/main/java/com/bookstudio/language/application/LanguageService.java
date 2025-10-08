package com.bookstudio.language.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstudio.language.domain.model.Language;
import com.bookstudio.language.infrastructure.repository.LanguageRepository;
import com.bookstudio.shared.application.dto.response.OptionResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LanguageService {
    private final LanguageRepository languageRepository;

    public List<OptionResponse> getOptions() {
        return languageRepository.findForOptions();
    }

    public Optional<Language> findById(Long languageId) {
        return languageRepository.findById(languageId);
    }
}
