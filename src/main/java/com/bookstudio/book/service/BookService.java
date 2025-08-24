package com.bookstudio.book.service;

import com.bookstudio.author.model.Author;
import com.bookstudio.book.dto.BookDetailDto;
import com.bookstudio.book.dto.BookListDto;
import com.bookstudio.book.dto.BookSelectDto;
import com.bookstudio.book.dto.BookSummaryDto;
import com.bookstudio.book.dto.CreateBookDto;
import com.bookstudio.book.dto.UpdateBookDto;
import com.bookstudio.book.model.Book;
import com.bookstudio.book.relation.BookAuthor;
import com.bookstudio.book.relation.BookAuthorId;
import com.bookstudio.book.relation.BookGenre;
import com.bookstudio.book.relation.BookGenreId;
import com.bookstudio.book.repository.BookAuthorRepository;
import com.bookstudio.book.repository.BookGenreRepository;
import com.bookstudio.book.repository.BookRepository;
import com.bookstudio.category.service.CategoryService;
import com.bookstudio.copy.repository.CopyRepository;
import com.bookstudio.genre.model.Genre;
import com.bookstudio.language.service.LanguageService;
import com.bookstudio.publisher.service.PublisherService;
import com.bookstudio.shared.util.SelectOptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CopyRepository copyRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final BookGenreRepository bookGenreRepository;

    private final LanguageService languageService;
    private final PublisherService publisherService;
    private final CategoryService categoryService;

    public List<BookListDto> getList() {
        return bookRepository.findList();
    }

    public Optional<Book> findById(Long bookId) {
        return bookRepository.findById(bookId);
    }

    public BookDetailDto getInfoById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + bookId));

        return BookDetailDto.builder()
                .id(book.getBookId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .language(languageService.toSummaryDto(book.getLanguage()))
                .edition(book.getEdition())
                .pages(book.getPages())
                .description(book.getDescription())
                .coverUrl(book.getCoverUrl())
                .publisher(publisherService.toSummaryDto(book.getPublisher()))
                .category(categoryService.toSummaryDto(book.getCategory()))
                .releaseDate(book.getReleaseDate())
                .status(book.getStatus().name())
                .authors(bookAuthorRepository.findAuthorSummariesByBookId(book.getBookId()))
                .genres(bookGenreRepository.findGenreSummariesByBookId(book.getBookId()))
                .build();
    }

    @Transactional
    public BookListDto create(CreateBookDto dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setEdition(dto.getEdition());
        book.setPages(dto.getPages());
        book.setDescription(dto.getDescription());
        book.setCoverUrl(dto.getCoverUrl());
        book.setReleaseDate(dto.getReleaseDate());
        book.setStatus(dto.getStatus());

        book.setLanguage(languageService.findById(dto.getLanguageId())
                .orElseThrow(() -> new EntityNotFoundException("Language not found with ID: " + dto.getLanguageId())));
        book.setPublisher(publisherService.findById(dto.getPublisherId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Publisher not found with ID: " + dto.getPublisherId())));
        book.setCategory(categoryService.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + dto.getCategoryId())));

        Book saved = bookRepository.save(book);

        if (dto.getAuthorIds() != null) {
            for (Long authorId : dto.getAuthorIds()) {
                BookAuthor relation = new BookAuthor();
                relation.setId(new BookAuthorId(saved.getBookId(), authorId));
                relation.setBook(saved);
                relation.setAuthor(new Author(authorId));
                bookAuthorRepository.save(relation);
            }
        }

        if (dto.getGenreIds() != null) {
            for (Long genreId : dto.getGenreIds()) {
                BookGenre relation = new BookGenre();
                relation.setId(new BookGenreId(saved.getBookId(), genreId));
                relation.setBook(saved);
                relation.setGenre(new Genre(genreId));
                bookGenreRepository.save(relation);
            }
        }

        return toListDto(saved);
    }

    @Transactional
    public BookListDto update(Long bookId, UpdateBookDto dto) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + bookId));

        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setEdition(dto.getEdition());
        book.setPages(dto.getPages());
        book.setDescription(dto.getDescription());
        book.setCoverUrl(dto.getCoverUrl());
        book.setReleaseDate(dto.getReleaseDate());
        book.setStatus(dto.getStatus());

        book.setLanguage(languageService.findById(dto.getLanguageId())
                .orElseThrow(() -> new EntityNotFoundException("Language not found with ID: " + dto.getLanguageId())));
        book.setPublisher(publisherService.findById(dto.getPublisherId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Publisher not found with ID: " + dto.getPublisherId())));
        book.setCategory(categoryService.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + dto.getCategoryId())));

        Book saved = bookRepository.save(book);

        bookAuthorRepository.deleteAllByBook(saved);
        bookGenreRepository.deleteAllByBook(saved);

        if (dto.getAuthorIds() != null) {
            for (Long authorId : dto.getAuthorIds()) {
                BookAuthor relation = BookAuthor.builder()
                        .id(new BookAuthorId(saved.getBookId(), authorId))
                        .book(saved)
                        .author(new Author(authorId))
                        .build();
                bookAuthorRepository.save(relation);
            }
        }

        if (dto.getGenreIds() != null) {
            for (Long genreId : dto.getGenreIds()) {
                BookGenre relation = BookGenre.builder()
                        .id(new BookGenreId(saved.getBookId(), genreId))
                        .book(saved)
                        .genre(new Genre(genreId))
                        .build();
                bookGenreRepository.save(relation);
            }
        }

        return toListDto(saved);
    }

    public List<BookSelectDto> getForSelect() {
        return bookRepository.findForSelect();
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .languages(languageService.getForSelect())
                .publishers(publisherService.getForSelect())
                .categories(categoryService.getForSelect())
                .build();
    }

    public BookSummaryDto toSummaryDto(Book book) {
        return BookSummaryDto.builder()
                .isbn(book.getIsbn())
                .id(book.getBookId())
                .coverUrl(book.getCoverUrl())
                .title(book.getTitle())
                .build();
    }

    private BookListDto toListDto(Book book) {
        return new BookListDto(
                book.getBookId(),
                book.getIsbn(),
                book.getCoverUrl(),
                book.getTitle(),
                book.getCategory().getName(),
                book.getPublisher().getName(),
                book.getLanguage().getCode(),
                book.getLanguage().getName(),
                copyRepository.countByBookAndIsAvailableFalse(book),
                copyRepository.countByBookAndIsAvailableTrue(book),
                book.getStatus());
    }
}
