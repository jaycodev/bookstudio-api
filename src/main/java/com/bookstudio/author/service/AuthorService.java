package com.bookstudio.author.service;

import com.bookstudio.author.model.Author;
import com.bookstudio.author.repository.AuthorRepository;
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
public class AuthorService {

	private final AuthorRepository authorRepository;

	private final NationalityService nationalityService;
	private final LiteraryGenreService literaryGenreService;

	public List<Author> listAuthors() {
		return authorRepository.findAll();
	}

	public Optional<Author> getAuthor(Long authorId) {
		return authorRepository.findById(authorId);
	}

	@Transactional
	public Author createAuthor(Author author) {
		return authorRepository.save(author);
	}

	@Transactional
	public Author updateAuthor(Long authorId, Author updatedData) {
		return authorRepository.findById(authorId).map(author -> {
			author.setName(updatedData.getName());
			author.setNationality(updatedData.getNationality());
			author.setLiteraryGenre(updatedData.getLiteraryGenre());
			author.setBirthDate(updatedData.getBirthDate());
			author.setPhoto(updatedData.getPhoto());
			author.setBiography(updatedData.getBiography());
			author.setStatus(updatedData.getStatus());
			return authorRepository.save(author);
		}).orElseThrow(() -> new RuntimeException("Autor no encontrado con ID: " + authorId));
	}

	public List<Author> getAuthorsForSelect() {
		return authorRepository.findByStatus(Status.activo);
	}

	public SelectOptions populateSelects() {
		return SelectOptions.builder()
				.nationalities(nationalityService.getNationalitiesForSelect())
				.literaryGenres(literaryGenreService.getLiteraryGenresForSelect())
				.build();
	}
}
