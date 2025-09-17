package com.bookstudio.book.domain.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.bookstudio.shared.domain.model.type.Status;

public record BookDetailResponse(
        Long id,
        String title,
        String isbn,
        Language language,
        String edition,
        Integer pages,
        String description,
        String coverUrl,
        Publisher publisher,
        Category category,
        LocalDate releaseDate,
        Status status,
        List<AuthorItem> authors,
        List<GenreItem> genres) {

    public BookDetailResponse withAuthorsAndGenres(List<AuthorItem> authors, List<GenreItem> genres) {
        return new BookDetailResponse(
                id, title, isbn, language, edition, pages, description, coverUrl,
                publisher, category, releaseDate, status,
                authors, genres);
    }

    public record Language(
            Long id,
            String code,
            String name) {
    }

    public record Category(
            Long id,
            String name) {
    }

    public record Publisher(
            Long id,
            String name) {
    }

    public record AuthorItem(
            Long id,
            String name) {
    }

    public record GenreItem(
            Long id,
            String name) {
    }
}
