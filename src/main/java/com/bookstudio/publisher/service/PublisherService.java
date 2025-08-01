package com.bookstudio.publisher.service;

import com.bookstudio.genre.model.Genre;
import com.bookstudio.nationality.service.NationalityService;
import com.bookstudio.publisher.dto.CreatePublisherDto;
import com.bookstudio.publisher.dto.PublisherDetailDto;
import com.bookstudio.publisher.dto.PublisherListDto;
import com.bookstudio.publisher.dto.PublisherSelectDto;
import com.bookstudio.publisher.dto.PublisherSummaryDto;
import com.bookstudio.publisher.dto.UpdatePublisherDto;
import com.bookstudio.publisher.model.Publisher;
import com.bookstudio.publisher.relation.PublisherGenre;
import com.bookstudio.publisher.relation.PublisherGenreId;
import com.bookstudio.publisher.repository.PublisherGenreRepository;
import com.bookstudio.publisher.repository.PublisherRepository;
import com.bookstudio.shared.util.SelectOptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final PublisherGenreRepository publisherGenreRepository;

    private final NationalityService nationalityService;

    public List<PublisherListDto> getList() {
        return publisherRepository.findList();
    }

    public Optional<Publisher> findById(Long publisherId) {
        return publisherRepository.findById(publisherId);
    }

    public PublisherDetailDto getInfoById(Long publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found with ID: " + publisherId));

        return PublisherDetailDto.builder()
                .id(publisher.getPublisherId())
                .name(publisher.getName())
                .nationality(nationalityService.toSummaryDto(publisher.getNationality()))
                .foundationYear(publisher.getFoundationYear())
                .website(publisher.getWebsite())
                .address(publisher.getAddress())
                .status(publisher.getStatus())
                .photoUrl(publisher.getPhotoUrl())
                .genres(publisherGenreRepository.findGenreSummariesByPublisherId(publisherId))
                .build();
    }

    @Transactional
    public PublisherListDto create(CreatePublisherDto dto) {
        Publisher publisher = new Publisher();
        publisher.setName(dto.getName());
        publisher.setNationality(nationalityService.findById(dto.getNationalityId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Nationality not found with ID: " + dto.getNationalityId())));
        publisher.setFoundationYear(dto.getFoundationYear());
        publisher.setWebsite(dto.getWebsite());
        publisher.setAddress(dto.getAddress());
        publisher.setStatus(dto.getStatus());
        publisher.setPhotoUrl(dto.getPhotoUrl());

        Publisher saved = publisherRepository.save(publisher);

        if (dto.getGenreIds() != null) {
            for (Long genreId : dto.getGenreIds()) {
                PublisherGenre relation = new PublisherGenre();
                relation.setId(new PublisherGenreId(saved.getPublisherId(), genreId));
                relation.setPublisher(saved);
                relation.setGenre(new Genre(genreId));
                publisherGenreRepository.save(relation);
            }
        }

        return toListDto(saved);
    }

    @Transactional
    public PublisherListDto update(UpdatePublisherDto dto) {
        Publisher publisher = publisherRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found with ID: " + dto.getId()));

        publisher.setName(dto.getName());
        publisher.setNationality(nationalityService.findById(dto.getNationalityId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Nationality not found with ID: " + dto.getNationalityId())));
        publisher.setFoundationYear(dto.getFoundationYear());
        publisher.setWebsite(dto.getWebsite());
        publisher.setAddress(dto.getAddress());
        publisher.setStatus(dto.getStatus());

        if (dto.getPhotoUrl() == null || dto.getPhotoUrl().isBlank()) {
            publisher.setPhotoUrl(null);
        } else {
            publisher.setPhotoUrl(dto.getPhotoUrl());
        }

        Publisher saved = publisherRepository.save(publisher);

        publisherGenreRepository.deleteAllByPublisher(saved);

        if (dto.getGenreIds() != null) {
            for (Long genreId : dto.getGenreIds()) {
                PublisherGenre relation = PublisherGenre.builder()
                        .id(new PublisherGenreId(saved.getPublisherId(), genreId))
                        .publisher(saved)
                        .genre(new Genre(genreId))
                        .build();
                publisherGenreRepository.save(relation);
            }
        }

        return toListDto(saved);
    }

    public List<PublisherSelectDto> getForSelect() {
        return publisherRepository.findForSelect();
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .nationalities(nationalityService.getForSelect())
                .build();
    }

    public PublisherSummaryDto toSummaryDto(Publisher publisher) {
        return PublisherSummaryDto.builder()
                .id(publisher.getPublisherId())
                .name(publisher.getName())
                .build();
    }

    private PublisherListDto toListDto(Publisher publisher) {
        return new PublisherListDto(
                publisher.getPhotoUrl(),
                publisher.getName(),
                publisher.getNationality().getName(),
                publisher.getWebsite(),
                publisher.getStatus(),
                publisher.getPublisherId());
    }
}
