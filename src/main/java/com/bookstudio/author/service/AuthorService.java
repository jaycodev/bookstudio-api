package com.bookstudio.author.service;

import com.bookstudio.author.dto.AuthorResponseDto;
import com.bookstudio.author.dto.CreateAuthorDto;
import com.bookstudio.author.dto.UpdateAuthorDto;
import com.bookstudio.author.model.Author;
import com.bookstudio.author.projection.AuthorInfoProjection;
import com.bookstudio.author.projection.AuthorListProjection;
import com.bookstudio.author.projection.AuthorSelectProjection;
import com.bookstudio.author.repository.AuthorRepository;
import com.bookstudio.nationality.service.NationalityService;
import com.bookstudio.shared.util.SelectOptions;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    private final NationalityService nationalityService;

    public List<AuthorListProjection> getList() {
        return authorRepository.findList();
    }

    public Optional<Author> findById(Long authorId) {
        return authorRepository.findById(authorId);
    }

    public Optional<AuthorInfoProjection> getInfoById(Long authorId) {
        return authorRepository.findInfoById(authorId);
    }

    @Transactional
    public AuthorResponseDto create(CreateAuthorDto dto) {
        Author author = new Author();
        author.setName(dto.getName());
        author.setNationality(nationalityService.findById(dto.getNationalityId())
                .orElseThrow(() -> new RuntimeException("Nacionalidad no encontrada")));
        author.setBirthDate(dto.getBirthDate());
        author.setBiography(dto.getBiography());
        author.setStatus(dto.getStatus());
        author.setPhotoUrl(dto.getPhotoUrl());

        Author saved = authorRepository.save(author);

        return new AuthorResponseDto(
                saved.getAuthorId(),
                saved.getName(),
                saved.getNationality().getName(),
                saved.getBirthDate(),
                saved.getStatus().name(),
                saved.getPhotoUrl());
    }

    @Transactional
    public AuthorResponseDto update(UpdateAuthorDto dto) {
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Autor no encontrado con ID: " + dto.getAuthorId()));

        author.setName(dto.getName());
        author.setNationality(nationalityService.findById(dto.getNationalityId())
                .orElseThrow(() -> new RuntimeException("Nacionalidad no encontrada")));
        author.setBirthDate(dto.getBirthDate());
        author.setBiography(dto.getBiography());
        author.setStatus(dto.getStatus());

        if (dto.isDeletePhoto()) {
            author.setPhotoUrl(null);
        } else if (dto.getPhotoUrl() != null && !dto.getPhotoUrl().isBlank()) {
            author.setPhotoUrl(dto.getPhotoUrl());
        }

        Author saved = authorRepository.save(author);

        return new AuthorResponseDto(
                saved.getAuthorId(),
                saved.getName(),
                saved.getNationality().getName(),
                saved.getBirthDate(),
                saved.getStatus().name(),
                saved.getPhotoUrl());
    }

    public List<AuthorSelectProjection> getForSelect() {
        return authorRepository.findForSelect();
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .nationalities(nationalityService.getForSelect())
                .build();
    }
}
