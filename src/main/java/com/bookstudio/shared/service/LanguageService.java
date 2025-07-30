package com.bookstudio.shared.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bookstudio.shared.model.Language;
import com.bookstudio.shared.repository.LanguageRepository;

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
}
