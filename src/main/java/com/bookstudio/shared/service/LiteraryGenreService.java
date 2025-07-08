package com.bookstudio.shared.service;

import com.bookstudio.shared.model.LiteraryGenre;
import com.bookstudio.shared.repository.LiteraryGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LiteraryGenreService {

    private final LiteraryGenreRepository literaryGenreRepository;

    public List<LiteraryGenre> getLiteraryGenresForSelect() {
        return literaryGenreRepository.findAll();
    }
}
