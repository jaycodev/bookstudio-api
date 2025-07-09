package com.bookstudio.book.service;

import com.bookstudio.author.service.AuthorService;
import com.bookstudio.book.dto.BookResponseDto;
import com.bookstudio.book.dto.CreateBookDto;
import com.bookstudio.book.dto.UpdateBookDto;
import com.bookstudio.book.model.Book;
import com.bookstudio.book.projection.BookInfoProjection;
import com.bookstudio.book.projection.BookListProjection;
import com.bookstudio.book.projection.BookSelectProjection;
import com.bookstudio.book.repository.BookRepository;
import com.bookstudio.course.service.CourseService;
import com.bookstudio.publisher.service.PublisherService;
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

    public List<BookListProjection> getList() {
        return bookRepository.findList();
    }

    public Optional<Book> findById(Long bookId) {
        return bookRepository.findById(bookId);
    }

    public Optional<BookInfoProjection> getInfoById(Long bookId) {
        return bookRepository.findInfoById(bookId);
    }

    @Transactional
    public BookResponseDto create(CreateBookDto dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setTotalCopies(dto.getTotalCopies());
        book.setReleaseDate(dto.getReleaseDate());
        book.setStatus(dto.getStatus());
        book.setLoanedCopies(0);

        book.setAuthor(authorService.findById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found")));
        book.setPublisher(publisherService.findById(dto.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found")));
        book.setCourse(courseService.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found")));
        book.setGenre(genreService.findById(dto.getGenreId())
                .orElseThrow(() -> new RuntimeException("Genre not found")));

        Book saved = bookRepository.save(book);

        return new BookResponseDto(
                saved.getId(),
                saved.getTitle(),
                saved.getAvailableCopies(),
                saved.getLoanedCopies(),
                String.valueOf(saved.getAuthor().getId()),
                saved.getAuthor().getName(),
                String.valueOf(saved.getPublisher().getId()),
                saved.getPublisher().getName(),
                saved.getStatus().name());
    }

    @Transactional
    public BookResponseDto update(UpdateBookDto dto) {
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + dto.getBookId()));

        book.setTitle(dto.getTitle());
        book.setTotalCopies(dto.getTotalCopies());
        book.setReleaseDate(dto.getReleaseDate());
        book.setStatus(dto.getStatus());

        book.setAuthor(authorService.findById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found")));
        book.setPublisher(publisherService.findById(dto.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found")));
        book.setCourse(courseService.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found")));
        book.setGenre(genreService.findById(dto.getGenreId())
                .orElseThrow(() -> new RuntimeException("Genre not found")));

        Book saved = bookRepository.save(book);

        return new BookResponseDto(
                saved.getId(),
                saved.getTitle(),
                saved.getAvailableCopies(),
                saved.getLoanedCopies(),
                String.valueOf(saved.getAuthor().getId()),
                saved.getAuthor().getName(),
                String.valueOf(saved.getPublisher().getId()),
                saved.getPublisher().getName(),
                saved.getStatus().name());
    }

    public List<BookSelectProjection> getForSelect() {
        return bookRepository.findForSelect();
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .authors(authorService.getForSelect())
                .publishers(publisherService.getForSelect())
                .courses(courseService.getForSelect())
                .genres(genreService.getForSelect())
                .build();
    }
}
