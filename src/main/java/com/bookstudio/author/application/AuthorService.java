package com.bookstudio.author.application;

import com.bookstudio.author.application.dto.request.CreateAuthorRequest;
import com.bookstudio.author.application.dto.request.UpdateAuthorRequest;
import com.bookstudio.author.application.dto.response.AuthorDetailResponse;
import com.bookstudio.author.application.dto.response.AuthorListResponse;
import com.bookstudio.author.application.dto.response.AuthorSelectOptionsResponse;
import com.bookstudio.author.domain.model.Author;
import com.bookstudio.author.infrastructure.repository.AuthorRepository;
import com.bookstudio.nationality.application.NationalityService;
import com.bookstudio.shared.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorService {
    private final AuthorRepository authorRepository;

    private final NationalityService nationalityService;

    public List<AuthorListResponse> getList() {
        return authorRepository.findList();
    }

    public AuthorSelectOptionsResponse getSelectOptions() {
        return new AuthorSelectOptionsResponse(nationalityService.getOptions());
    }

    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }

    public AuthorDetailResponse getDetailById(Long id) {
        return authorRepository.findDetailById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + id));
    }

    @Transactional
    public AuthorListResponse create(CreateAuthorRequest request) {
        Author author = new Author();
        author.setNationality(nationalityService.findById(request.nationalityId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Nationality not found with ID: " + request.nationalityId())));

        author.setName(request.name());
        author.setBirthDate(request.birthDate());
        author.setBiography(request.biography());
        author.setStatus(request.status());
        author.setPhotoUrl(request.photoUrl());

        Author saved = authorRepository.save(author);

        return toListResponse(saved);
    }

    @Transactional
    public AuthorListResponse update(Long id, UpdateAuthorRequest request) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + id));

        author.setNationality(nationalityService.findById(request.nationalityId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Nationality not found with ID: " + request.nationalityId())));

        author.setName(request.name());
        author.setBirthDate(request.birthDate());
        author.setBiography(request.biography());
        author.setStatus(request.status());
        author.setPhotoUrl(request.photoUrl());

        Author updated = authorRepository.save(author);

        return toListResponse(updated);
    }

    private AuthorListResponse toListResponse(Author author) {
        return new AuthorListResponse(
                author.getId(),
                author.getPhotoUrl(),
                author.getName(),

                author.getNationality().getId(),
                author.getNationality().getCode(),
                author.getNationality().getName(),

                author.getBirthDate(),
                author.getStatus());
    }
}
