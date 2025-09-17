package com.bookstudio.book.application.dto.response;

import java.util.LinkedHashMap;
import java.util.Map;

import com.bookstudio.shared.domain.model.type.Status;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "isbn", "coverUrl", "title", "category", "publisher", "language", "copies", "status" })
public record BookListResponse(
        Long id,
        String isbn,
        String coverUrl,
        String title,

        @JsonIgnore Long categoryId,
        @JsonIgnore String categoryName,

        @JsonIgnore Long publisherId,
        @JsonIgnore String publisherName,

        @JsonIgnore Long languageId,
        @JsonIgnore String languageCode,
        @JsonIgnore String languageName,

        @JsonIgnore Long copiesLoaned,
        @JsonIgnore Long copiesAvailable,

        Status status) {

    @JsonGetter("category")
    public Map<String, Object> getCategory() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", categoryId());
        map.put("name", categoryName());
        return map;
    }

    @JsonGetter("publisher")
    public Map<String, Object> getPublisher() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", publisherId());
        map.put("name", publisherName());
        return map;
    }

    @JsonGetter("language")
    public Map<String, Object> getLanguage() {
        Map<String, Object> language = new LinkedHashMap<>();
        language.put("id", languageId);
        language.put("code", languageCode);
        language.put("name", languageName);
        return language;
    }

    @JsonGetter("copies")
    public Map<String, Object> getCopies() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("loaned", copiesLoaned());
        map.put("available", copiesAvailable());
        return map;
    }
}
