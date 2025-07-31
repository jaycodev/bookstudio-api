package com.bookstudio.genre.service;

import com.bookstudio.genre.dto.GenreDto;
import com.bookstudio.genre.model.Genre;
import com.bookstudio.genre.repository.GenreRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public List<Genre> getForSelect() {
        return genreRepository.findAllByOrderByNameAsc();
    }

    public Optional<Genre> findById(Long genreId) {
        return genreRepository.findById(genreId);
    }

    public GenreDto toDto(Genre genre) {
        return GenreDto.builder()
                .id(genre.getGenreId())
                .name(genre.getName())
                .build();
    }
}
