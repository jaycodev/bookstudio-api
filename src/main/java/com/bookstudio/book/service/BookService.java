package com.bookstudio.book.service;

import com.bookstudio.book.dto.BookResponseDto;
import com.bookstudio.book.dto.CreateBookDto;
import com.bookstudio.book.dto.UpdateBookDto;
import com.bookstudio.book.model.Book;
import com.bookstudio.book.projection.BookInfoProjection;
import com.bookstudio.book.projection.BookListProjection;
import com.bookstudio.book.projection.BookSelectProjection;
import com.bookstudio.book.repository.BookRepository;
import com.bookstudio.category.service.CategoryService;
import com.bookstudio.publisher.service.PublisherService;
import com.bookstudio.shared.service.LanguageService;
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

    private final LanguageService languageService;
    private final PublisherService publisherService;
    private final CategoryService categoryService;

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
        book.setIsbn(dto.getIsbn());
        book.setEdition(dto.getEdition());
        book.setPages(dto.getPages());
        book.setDescription(dto.getDescription());
        book.setCoverUrl(dto.getCoverUrl());
        book.setReleaseDate(dto.getReleaseDate());
        book.setStatus(dto.getStatus());

        book.setLanguage(languageService.findById(dto.getLanguageId())
                .orElseThrow(() -> new RuntimeException("Language not found")));
        book.setPublisher(publisherService.findById(dto.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found")));
        book.setCategory(categoryService.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));

        Book saved = bookRepository.save(book);

        return new BookResponseDto(
                saved.getBookId(),
                saved.getTitle(),
                saved.getIsbn(),
                saved.getCoverUrl(),
                saved.getPublisher().getName(),
                saved.getCategory().getName(),
                saved.getStatus().name());
    }

    @Transactional
    public BookResponseDto update(UpdateBookDto dto) {
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + dto.getBookId()));

        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setEdition(dto.getEdition());
        book.setPages(dto.getPages());
        book.setDescription(dto.getDescription());
        book.setCoverUrl(dto.getCoverUrl());
        book.setReleaseDate(dto.getReleaseDate());
        book.setStatus(dto.getStatus());

        book.setLanguage(languageService.findById(dto.getLanguageId())
                .orElseThrow(() -> new RuntimeException("Language not found")));
        book.setPublisher(publisherService.findById(dto.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found")));
        book.setCategory(categoryService.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));

        Book saved = bookRepository.save(book);

        return new BookResponseDto(
                saved.getBookId(),
                saved.getTitle(),
                saved.getIsbn(),
                saved.getCoverUrl(),
                saved.getPublisher().getName(),
                saved.getCategory().getName(),
                saved.getStatus().name());
    }

    public List<BookSelectProjection> getForSelect() {
        return bookRepository.findForSelect();
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .languages(languageService.getForSelect())
                .publishers(publisherService.getForSelect())
                .categories(categoryService.getForSelect())
                .build();
    }
}
