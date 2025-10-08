package com.bookstudio.publisher.application;

import com.bookstudio.genre.domain.model.Genre;
import com.bookstudio.nationality.application.NationalityService;
import com.bookstudio.publisher.application.dto.request.CreatePublisherRequest;
import com.bookstudio.publisher.application.dto.request.UpdatePublisherRequest;
import com.bookstudio.publisher.application.dto.response.PublisherDetailResponse;
import com.bookstudio.publisher.application.dto.response.PublisherListResponse;
import com.bookstudio.publisher.domain.model.Publisher;
import com.bookstudio.publisher.domain.model.PublisherGenre;
import com.bookstudio.publisher.domain.model.PublisherGenreId;
import com.bookstudio.publisher.infrastructure.repository.PublisherGenreRepository;
import com.bookstudio.publisher.infrastructure.repository.PublisherRepository;
import com.bookstudio.shared.application.dto.response.OptionResponse;
import com.bookstudio.shared.util.SelectOptions;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final PublisherGenreRepository publisherGenreRepository;

    private final NationalityService nationalityService;

    public List<PublisherListResponse> getList() {
        return publisherRepository.findList();
    }

    public List<OptionResponse> getOptions() {
        return publisherRepository.findForOptions();
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .nationalities(nationalityService.getOptions())
                .build();
    }

    public Optional<Publisher> findById(Long id) {
        return publisherRepository.findById(id);
    }

    public PublisherDetailResponse getDetailById(Long id) {
        PublisherDetailResponse base = publisherRepository.findDetailById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found with ID: " + id));

        return base.withGenres(publisherGenreRepository.findGenreItemsByPublisherId(id));
    }

    @Transactional
    public PublisherListResponse create(CreatePublisherRequest request) {
        Publisher publisher = new Publisher();
        publisher.setNationality(nationalityService.findById(request.nationalityId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Nationality not found with ID: " + request.nationalityId())));

        publisher.setName(request.name());
        publisher.setFoundationYear(request.foundationYear());
        publisher.setWebsite(request.website());
        publisher.setAddress(request.address());
        publisher.setStatus(request.status());
        publisher.setPhotoUrl(request.photoUrl());

        Publisher saved = publisherRepository.save(publisher);

        if (request.genreIds() != null) {
            for (Long genreId : request.genreIds()) {
                PublisherGenre relation = PublisherGenre.builder()
                        .id(new PublisherGenreId(saved.getId(), genreId))
                        .publisher(saved)
                        .genre(new Genre(genreId))
                        .build();
                publisherGenreRepository.save(relation);
            }
        }

        return toListResponse(saved);
    }

    @Transactional
    public PublisherListResponse update(Long id, UpdatePublisherRequest request) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found with ID: " + id));

        publisher.setNationality(nationalityService.findById(request.nationalityId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Nationality not found with ID: " + request.nationalityId())));

        publisher.setName(request.name());
        publisher.setFoundationYear(request.foundationYear());
        publisher.setWebsite(request.website());
        publisher.setAddress(request.address());
        publisher.setStatus(request.status());

        if (request.photoUrl() == null || request.photoUrl().isBlank()) {
            publisher.setPhotoUrl(null);
        } else {
            publisher.setPhotoUrl(request.photoUrl());
        }

        Publisher updated = publisherRepository.save(publisher);

        publisherGenreRepository.deleteAllByPublisher(updated);

        if (request.genreIds() != null) {
            for (Long genreId : request.genreIds()) {
                PublisherGenre relation = PublisherGenre.builder()
                        .id(new PublisherGenreId(updated.getId(), genreId))
                        .publisher(updated)
                        .genre(new Genre(genreId))
                        .build();
                publisherGenreRepository.save(relation);
            }
        }

        return toListResponse(updated);
    }

    private PublisherListResponse toListResponse(Publisher publisher) {
        return new PublisherListResponse(
                publisher.getId(),
                publisher.getPhotoUrl(),
                publisher.getName(),

                publisher.getNationality().getId(),
                publisher.getNationality().getCode(),
                publisher.getNationality().getName(),

                publisher.getWebsite(),
                publisher.getAddress(),
                publisher.getStatus());
    }
}
