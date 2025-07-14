package com.bookstudio.publisher.service;

import com.bookstudio.publisher.dto.CreatePublisherDto;
import com.bookstudio.publisher.dto.PublisherResponseDto;
import com.bookstudio.publisher.dto.UpdatePublisherDto;
import com.bookstudio.publisher.model.Publisher;
import com.bookstudio.publisher.projection.PublisherInfoProjection;
import com.bookstudio.publisher.projection.PublisherListProjection;
import com.bookstudio.publisher.projection.PublisherSelectProjection;
import com.bookstudio.publisher.repository.PublisherRepository;
import com.bookstudio.shared.service.LiteraryGenreService;
import com.bookstudio.shared.service.NationalityService;
import com.bookstudio.shared.util.SelectOptions;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;

    private final NationalityService nationalityService;
    private final LiteraryGenreService literaryGenreService;

    public List<PublisherListProjection> getList() {
        return publisherRepository.findList();
    }

    public Optional<Publisher> findById(Long publisherId) {
        return publisherRepository.findById(publisherId);
    }

    public Optional<PublisherInfoProjection> getInfoById(Long publisherId) {
        return publisherRepository.findInfoById(publisherId);
    }

    @Transactional
    public PublisherResponseDto create(CreatePublisherDto dto) {
        Publisher publisher = new Publisher();
        publisher.setName(dto.getName());
        publisher.setNationality(nationalityService.findById(dto.getNationalityId())
                .orElseThrow(() -> new RuntimeException("Nationality not found")));
        publisher.setLiteraryGenre(literaryGenreService.findById(dto.getLiteraryGenreId())
                .orElseThrow(() -> new RuntimeException("Literary genre not found")));
        publisher.setFoundationYear(dto.getFoundationYear());
        publisher.setWebsite(dto.getWebsite());
        publisher.setAddress(dto.getAddress());
        publisher.setStatus(dto.getStatus());
        publisher.setPhotoUrl(dto.getPhotoUrl());

        Publisher saved = publisherRepository.save(publisher);

        return new PublisherResponseDto(
                saved.getId(),
                saved.getName(),
                saved.getNationality().getName(),
                saved.getLiteraryGenre().getName(),
                saved.getWebsite(),
                saved.getStatus().name(),
                saved.getPhotoUrl());
    }

    @Transactional
    public PublisherResponseDto update(UpdatePublisherDto dto) {
        Publisher publisher = publisherRepository.findById(dto.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Editorial no encontrada con ID: " + dto.getPublisherId()));

        publisher.setName(dto.getName());
        publisher.setNationality(nationalityService.findById(dto.getNationalityId())
                .orElseThrow(() -> new RuntimeException("Nationality not found")));
        publisher.setLiteraryGenre(literaryGenreService.findById(dto.getLiteraryGenreId())
                .orElseThrow(() -> new RuntimeException("Literary genre not found")));
        publisher.setFoundationYear(dto.getFoundationYear());
        publisher.setWebsite(dto.getWebsite());
        publisher.setAddress(dto.getAddress());
        publisher.setStatus(dto.getStatus());

        if (dto.isDeletePhoto()) {
            publisher.setPhotoUrl(null);
        } else if (dto.getPhotoUrl() != null && !dto.getPhotoUrl().isBlank()) {
            publisher.setPhotoUrl(dto.getPhotoUrl());
        }

        Publisher saved = publisherRepository.save(publisher);

        return new PublisherResponseDto(
                saved.getId(),
                saved.getName(),
                saved.getNationality().getName(),
                saved.getLiteraryGenre().getName(),
                saved.getWebsite(),
                saved.getStatus().name(),
                saved.getPhotoUrl());
    }

    public List<PublisherSelectProjection> getForSelect() {
        return publisherRepository.findForSelect();
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .nationalities(nationalityService.getForSelect())
                .literaryGenres(literaryGenreService.getForSelect())
                .build();
    }
}
