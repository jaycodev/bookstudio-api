package com.bookstudio.shared.service;

import com.bookstudio.shared.model.Genre;
import com.bookstudio.shared.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public List<Genre> getForSelect() {
        return genreRepository.findAll();
    }

    public Optional<Genre> findById(Long genreId) {
        return genreRepository.findById(genreId);
    }
}
