package com.bookstudio.book.application;

import com.bookstudio.author.domain.model.Author;
import com.bookstudio.book.application.dto.request.CreateBookRequest;
import com.bookstudio.book.application.dto.request.UpdateBookRequest;
import com.bookstudio.book.application.dto.response.BookDetailResponse;
import com.bookstudio.book.application.dto.response.BookListResponse;
import com.bookstudio.book.domain.model.Book;
import com.bookstudio.book.domain.model.BookAuthor;
import com.bookstudio.book.domain.model.BookAuthorId;
import com.bookstudio.book.domain.model.BookGenre;
import com.bookstudio.book.domain.model.BookGenreId;
import com.bookstudio.book.infrastructure.repository.BookAuthorRepository;
import com.bookstudio.book.infrastructure.repository.BookGenreRepository;
import com.bookstudio.book.infrastructure.repository.BookRepository;
import com.bookstudio.category.application.CategoryService;
import com.bookstudio.copy.domain.model.Copy;
import com.bookstudio.copy.domain.model.type.CopyStatus;
import com.bookstudio.genre.domain.model.Genre;
import com.bookstudio.language.application.LanguageService;
import com.bookstudio.publisher.application.PublisherService;
import com.bookstudio.shared.exception.ResourceNotFoundException;
import com.bookstudio.shared.response.OptionResponse;
import com.bookstudio.shared.util.SelectOptions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final BookGenreRepository bookGenreRepository;

    private final LanguageService languageService;
    private final PublisherService publisherService;
    private final CategoryService categoryService;

    public List<BookListResponse> getList() {
        return bookRepository.findList();
    }

    public List<OptionResponse> getOptions() {
        return bookRepository.findForOptions();
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .languages(languageService.getOptions())
                .publishers(publisherService.getOptions())
                .categories(categoryService.getOptions())
                .build();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public BookDetailResponse getDetailById(Long id) {
        BookDetailResponse base = bookRepository.findDetailById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));

        return base.withAuthorsAndGenres(
                bookAuthorRepository.findAuthorItemsByBookId(id),
                bookGenreRepository.findGenreItemsByBookId(id));
    }

    @Transactional
    public BookListResponse create(CreateBookRequest request) {
        Book book = new Book();
        book.setLanguage(languageService.findById(request.languageId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Language not found with ID: " + request.languageId())));
        book.setPublisher(publisherService.findById(request.publisherId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Publisher not found with ID: " + request.publisherId())));
        book.setCategory(categoryService.findById(request.categoryId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Category not found with ID: " + request.categoryId())));

        book.setTitle(request.title());
        book.setIsbn(request.isbn());
        book.setEdition(request.edition());
        book.setPages(request.pages());
        book.setDescription(request.description());
        book.setCoverUrl(request.coverUrl());
        book.setReleaseDate(request.releaseDate());
        book.setStatus(request.status());

        Book saved = bookRepository.save(book);

        if (request.authorIds() != null) {
            for (Long authorId : request.authorIds()) {
                BookAuthor relation = BookAuthor.builder()
                        .id(new BookAuthorId(saved.getId(), authorId))
                        .book(saved)
                        .author(new Author(authorId))
                        .build();
                bookAuthorRepository.save(relation);
            }
        }

        if (request.genreIds() != null) {
            for (Long genreId : request.genreIds()) {
                BookGenre relation = BookGenre.builder()
                        .id(new BookGenreId(saved.getId(), genreId))
                        .book(saved)
                        .genre(new Genre(genreId))
                        .build();
                bookGenreRepository.save(relation);
            }
        }

        return toListResponse(saved);
    }

    @Transactional
    public BookListResponse update(Long id, UpdateBookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));

        book.setLanguage(languageService.findById(request.languageId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Language not found with ID: " + request.languageId())));
        book.setPublisher(publisherService.findById(request.publisherId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Publisher not found with ID: " + request.publisherId())));
        book.setCategory(categoryService.findById(request.categoryId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Category not found with ID: " + request.categoryId())));

        book.setTitle(request.title());
        book.setIsbn(request.isbn());
        book.setEdition(request.edition());
        book.setPages(request.pages());
        book.setDescription(request.description());
        book.setCoverUrl(request.coverUrl());
        book.setReleaseDate(request.releaseDate());
        book.setStatus(request.status());

        Book updated = bookRepository.save(book);

        bookAuthorRepository.deleteAllByBook(updated);
        bookGenreRepository.deleteAllByBook(updated);

        if (request.authorIds() != null) {
            for (Long authorId : request.authorIds()) {
                BookAuthor relation = BookAuthor.builder()
                        .id(new BookAuthorId(updated.getId(), authorId))
                        .book(updated)
                        .author(new Author(authorId))
                        .build();
                bookAuthorRepository.save(relation);
            }
        }

        if (request.genreIds() != null) {
            for (Long genreId : request.genreIds()) {
                BookGenre relation = BookGenre.builder()
                        .id(new BookGenreId(updated.getId(), genreId))
                        .book(updated)
                        .genre(new Genre(genreId))
                        .build();
                bookGenreRepository.save(relation);
            }
        }

        return toListResponse(updated);
    }

    private BookListResponse toListResponse(Book book) {
        Map<CopyStatus, Long> counts = book.getCopies().stream()
                .collect(Collectors.groupingBy(
                        Copy::getStatus,
                        Collectors.counting()));

        return new BookListResponse(
                book.getId(),
                book.getIsbn(),
                book.getCoverUrl(),
                book.getTitle(),

                book.getCategory().getId(),
                book.getCategory().getName(),

                book.getPublisher().getId(),
                book.getPublisher().getName(),

                book.getLanguage().getId(),
                book.getLanguage().getCode(),
                book.getLanguage().getName(),

                counts.entrySet().stream()
                        .filter(entry -> entry.getKey() != CopyStatus.DISPONIBLE)
                        .mapToLong(Map.Entry::getValue)
                        .sum(),
                counts.getOrDefault(CopyStatus.DISPONIBLE, 0L),

                book.getStatus());
    }
}
