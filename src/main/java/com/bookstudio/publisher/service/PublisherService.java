package com.bookstudio.publisher.service;

import com.bookstudio.publisher.model.Publisher;
import com.bookstudio.publisher.repository.PublisherRepository;
import com.bookstudio.shared.enums.Status;
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

	public List<Publisher> listPublishers() {
		return publisherRepository.findAll();
	}

	public Optional<Publisher> getPublisher(Long id) {
		return publisherRepository.findById(id);
	}

	@Transactional
	public Publisher createPublisher(Publisher publisher) {
		return publisherRepository.save(publisher);
	}

	@Transactional
	public Publisher updatePublisher(Long id, Publisher updatedData) {
		return publisherRepository.findById(id).map(publisher -> {
			publisher.setName(updatedData.getName());
			publisher.setNationality(updatedData.getNationality());
			publisher.setLiteraryGenre(updatedData.getLiteraryGenre());
			publisher.setPhoto(updatedData.getPhoto());
			publisher.setFoundationYear(updatedData.getFoundationYear());
			publisher.setWebsite(updatedData.getWebsite());
			publisher.setAddress(updatedData.getAddress());
			publisher.setStatus(updatedData.getStatus());
			return publisherRepository.save(publisher);
		}).orElseThrow(() -> new RuntimeException("Editorial no encontrada con ID: " + id));
	}

	public List<Publisher> getPublishersForSelect() {
		return publisherRepository.findByStatus(Status.activo);
	}

	public SelectOptions populateSelects() {
		return SelectOptions.builder()
				.nationalities(nationalityService.getNationalitiesForSelect())
				.literaryGenres(literaryGenreService.getLiteraryGenresForSelect())
				.build();
	}
}
