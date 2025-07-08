package com.bookstudio.book.service;

import com.bookstudio.author.service.AuthorService;
import com.bookstudio.book.model.Book;
import com.bookstudio.book.repository.BookRepository;
import com.bookstudio.course.service.CourseService;
import com.bookstudio.publisher.service.PublisherService;
import com.bookstudio.shared.enums.Status;
import com.bookstudio.shared.service.GenreService;
import com.bookstudio.shared.util.SelectOptions;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

	private final BookRepository bookRepository;

	private final AuthorService authorService;
	private final PublisherService publisherService;
	private final CourseService courseService;
	private final GenreService genreService;

	public List<Book> listBooks() {
		return bookRepository.findAll();
	}

	public Optional<Book> getBook(Long bookId) {
		return bookRepository.findById(bookId);
	}

	@Transactional
	public Book createBook(Book book) {
		book.setLoanedCopies(0);
		return bookRepository.save(book);
	}

	@Transactional
	public Book updateBook(Long bookId, Book updatedData) {
		return bookRepository.findById(bookId).map(book -> {
			book.setTitle(updatedData.getTitle());
			book.setTotalCopies(updatedData.getTotalCopies());
			book.setAuthor(updatedData.getAuthor());
			book.setPublisher(updatedData.getPublisher());
			book.setCourse(updatedData.getCourse());
			book.setReleaseDate(updatedData.getReleaseDate());
			book.setGenre(updatedData.getGenre());
			book.setStatus(updatedData.getStatus());
			return bookRepository.save(book);
		}).orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + bookId));
	}

	public List<Book> getBooksForSelect() {
		return bookRepository.findAvailableBooksByStatus(Status.activo);
	}

	public SelectOptions populateSelects() {
		return SelectOptions.builder()
				.authors(authorService.getAuthorsForSelect())
				.publishers(publisherService.getPublishersForSelect())
				.courses(courseService.getCoursesForSelect())
				.genres(genreService.getGenresForSelect())
				.build();
	}
}
