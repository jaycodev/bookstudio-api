package com.bookstudio.genre.application;

import com.bookstudio.genre.domain.model.Genre;
import com.bookstudio.genre.infrastructure.repository.GenreRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenreService {
    private final GenreRepository genreRepository;

    public Optional<Genre> findById(Long id) {
        return genreRepository.findById(id);
    }
}
