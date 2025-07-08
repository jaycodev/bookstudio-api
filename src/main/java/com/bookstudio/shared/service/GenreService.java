package com.bookstudio.shared.service;

import com.bookstudio.shared.model.Genre;
import com.bookstudio.shared.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public List<Genre> getGenresForSelect() {
        return genreRepository.findAll();
    }
}
