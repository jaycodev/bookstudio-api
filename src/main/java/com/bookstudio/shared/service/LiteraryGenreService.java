package com.bookstudio.shared.service;

import com.bookstudio.shared.model.LiteraryGenre;
import com.bookstudio.shared.repository.LiteraryGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LiteraryGenreService {

    private final LiteraryGenreRepository literaryGenreRepository;

    public List<LiteraryGenre> getForSelect() {
        return literaryGenreRepository.findAll();
    }

    public Optional<LiteraryGenre> findById(Long literaryGenreId) {
        return literaryGenreRepository.findById(literaryGenreId);
    }
}
