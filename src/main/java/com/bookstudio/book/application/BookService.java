package com.bookstudio.book.application;

import com.bookstudio.author.domain.model.Author;
import com.bookstudio.book.application.dto.request.CreateBookRequest;
import com.bookstudio.book.application.dto.request.UpdateBookRequest;
import com.bookstudio.book.application.dto.response.BookDetailResponse;
import com.bookstudio.book.application.dto.response.BookFilterOptionsResponse;
import com.bookstudio.book.application.dto.response.BookListResponse;
import com.bookstudio.book.application.dto.response.BookSelectOptionsResponse;
import com.bookstudio.book.domain.model.Book;
import com.bookstudio.book.domain.model.BookAuthor;
import com.bookstudio.book.domain.model.BookAuthorId;
import com.bookstudio.book.domain.model.BookGenre;
import com.bookstudio.book.domain.model.BookGenreId;
import com.bookstudio.book.infrastructure.repository.BookAuthorRepository;
import com.bookstudio.book.infrastructure.repository.BookGenreRepository;
import com.bookstudio.book.infrastructure.repository.BookRepository;
import com.bookstudio.category.infrastructure.repository.CategoryRepository;
import com.bookstudio.copy.domain.model.Copy;
import com.bookstudio.copy.domain.model.type.CopyStatus;
import com.bookstudio.genre.domain.model.Genre;
import com.bookstudio.language.infrastructure.repository.LanguageRepository;
import com.bookstudio.loan.infrastructure.repository.LoanRepository;
import com.bookstudio.publisher.infrastructure.repository.PublisherRepository;
import com.bookstudio.shared.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final BookGenreRepository bookGenreRepository;
    
    private final LoanRepository loanRepository;
    private final LanguageRepository languageRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;

    public List<BookListResponse> getList() {
        return bookRepository.findList();
    }

    public BookFilterOptionsResponse getFilterOptions() {
        return new BookFilterOptionsResponse(
                categoryRepository.findForOptions(),
                publisherRepository.findForOptions(),
                languageRepository.findForOptions(),
                loanRepository.findForOptions());
    }

    public BookSelectOptionsResponse getSelectOptions() {
        return new BookSelectOptionsResponse(
                languageRepository.findForOptions(),
                publisherRepository.findForOptions(),
                categoryRepository.findForOptions());
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
        book.setLanguage(languageRepository.findById(request.languageId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Language not found with ID: " + request.languageId())));
        book.setPublisher(publisherRepository.findById(request.publisherId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Publisher not found with ID: " + request.publisherId())));
        book.setCategory(categoryRepository.findById(request.categoryId())
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

        book.setLanguage(languageRepository.findById(request.languageId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Language not found with ID: " + request.languageId())));
        book.setPublisher(publisherRepository.findById(request.publisherId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Publisher not found with ID: " + request.publisherId())));
        book.setCategory(categoryRepository.findById(request.categoryId())
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
