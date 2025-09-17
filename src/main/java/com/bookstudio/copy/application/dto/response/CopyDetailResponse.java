package com.bookstudio.copy.application.dto.response;

import java.util.LinkedHashMap;
import java.util.Map;

import com.bookstudio.copy.domain.model.type.CopyCondition;
import com.bookstudio.copy.domain.model.type.CopyStatus;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "code", "book", "shelf", "barcode", "status", "condition" })
public record CopyDetailResponse(
        Long id,
        String code,

        @JsonIgnore Long bookId,
        @JsonIgnore String bookIsbn,
        @JsonIgnore String bookCoverUrl,
        @JsonIgnore String bookTitle,

        @JsonIgnore Long shelfId,
        @JsonIgnore String shelfCode,
        @JsonIgnore String shelfFloor,
        @JsonIgnore String shelfDescription,

        String barcode,
        CopyStatus status,
        CopyCondition condition) {

    @JsonGetter("book")
    public Map<String, Object> getBook() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", bookId());
        map.put("isbn", bookIsbn());
        map.put("coverUrl", bookCoverUrl());
        map.put("title", bookTitle());
        return map;
    }

    @JsonGetter("shelf")
    public Map<String, Object> getShelf() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", shelfId());
        map.put("code", shelfCode());
        map.put("floor", shelfFloor());
        map.put("description", shelfDescription());
        return map;
    }
}
