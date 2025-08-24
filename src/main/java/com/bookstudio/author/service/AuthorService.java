package com.bookstudio.author.service;

import com.bookstudio.author.dto.AuthorDetailDto;
import com.bookstudio.author.dto.AuthorListDto;
import com.bookstudio.author.dto.AuthorSelectDto;
import com.bookstudio.author.dto.AuthorSummaryDto;
import com.bookstudio.author.dto.CreateAuthorDto;
import com.bookstudio.author.dto.UpdateAuthorDto;
import com.bookstudio.author.model.Author;
import com.bookstudio.author.repository.AuthorRepository;
import com.bookstudio.nationality.service.NationalityService;
import com.bookstudio.shared.util.SelectOptions;

import jakarta.persistence.EntityNotFoundException;
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

    public List<AuthorListDto> getList() {
        return authorRepository.findList();
    }

    public Optional<Author> findById(Long authorId) {
        return authorRepository.findById(authorId);
    }

    public AuthorDetailDto getInfoById(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found with ID: " + authorId));

        return AuthorDetailDto.builder()
                .id(author.getAuthorId())
                .name(author.getName())
                .nationality(nationalityService.toSummaryDto(author.getNationality()))
                .birthDate(author.getBirthDate())
                .biography(author.getBiography())
                .status(author.getStatus().name())
                .photoUrl(author.getPhotoUrl())
                .build();
    }

    @Transactional
    public AuthorListDto create(CreateAuthorDto dto) {
        Author author = new Author();
        author.setName(dto.getName());
        author.setNationality(nationalityService.findById(dto.getNationalityId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Nationality not found with ID: " + dto.getNationalityId())));
        author.setBirthDate(dto.getBirthDate());
        author.setBiography(dto.getBiography());
        author.setStatus(dto.getStatus());
        author.setPhotoUrl(dto.getPhotoUrl());

        Author saved = authorRepository.save(author);

        return toListDto(saved);
    }

    @Transactional
    public AuthorListDto update(Long authorId, UpdateAuthorDto dto) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with ID: " + authorId));

        author.setName(dto.getName());
        author.setNationality(nationalityService.findById(dto.getNationalityId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Nationality not found with ID: " + dto.getNationalityId())));
        author.setBirthDate(dto.getBirthDate());
        author.setBiography(dto.getBiography());
        author.setStatus(dto.getStatus());

        if (dto.isDeletePhoto()) {
            author.setPhotoUrl(null);
        } else if (dto.getPhotoUrl() != null && !dto.getPhotoUrl().isBlank()) {
            author.setPhotoUrl(dto.getPhotoUrl());
        }

        Author saved = authorRepository.save(author);

        return toListDto(saved);
    }

    public List<AuthorSelectDto> getForSelect() {
        return authorRepository.findForSelect();
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .nationalities(nationalityService.getForSelect())
                .build();
    }

    public AuthorSummaryDto toSummaryDto(Author author) {
        return AuthorSummaryDto.builder()
                .id(author.getAuthorId())
                .name(author.getName())
                .build();
    }

    private AuthorListDto toListDto(Author author) {
        return new AuthorListDto(
                author.getAuthorId(),
                author.getPhotoUrl(),
                author.getName(),
                author.getNationality().getName(),
                author.getBirthDate(),
                author.getStatus());
    }
}
