package com.bookstudio.book.application.dto.response;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bookstudio.shared.domain.model.type.Status;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "title", "isbn", "language", "edition", "pages", "description", "coverUrl", "publisher",
        "category", "releaseDate", "status", "authors", "genres" })
public record BookDetailResponse(
        Long id,
        String title,
        String isbn,

        @JsonIgnore Long languageId,
        @JsonIgnore String languageCode,
        @JsonIgnore String languageName,

        String edition,
        Integer pages,
        String description,
        String coverUrl,

        @JsonIgnore Long publisherId,
        @JsonIgnore String publisherName,

        @JsonIgnore Long categoryId,
        @JsonIgnore String categoryName,

        LocalDate releaseDate,
        Status status,

        List<AuthorItem> authors,
        List<GenreItem> genres) {

    public BookDetailResponse withAuthorsAndGenres(List<AuthorItem> authors, List<GenreItem> genres) {
        return new BookDetailResponse(
                id, title, isbn, languageId, languageCode, languageName, edition, pages, description, coverUrl,
                publisherId, publisherName, categoryId, categoryName, releaseDate, status, authors, genres);
    }

    @JsonGetter("language")
    public Map<String, Object> getNationality() {
        Map<String, Object> language = new LinkedHashMap<>();
        language.put("id", languageId);
        language.put("code", languageCode);
        language.put("name", languageName);
        return language;
    }

    @JsonGetter("publisher")
    public Map<String, Object> getPublisher() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", publisherId());
        map.put("name", publisherName());
        return map;
    }

    @JsonGetter("category")
    public Map<String, Object> getCategory() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", categoryId());
        map.put("name", categoryName());
        return map;
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
