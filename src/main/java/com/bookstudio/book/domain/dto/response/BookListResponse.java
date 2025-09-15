package com.bookstudio.book.domain.dto.response;

import com.bookstudio.shared.domain.model.Status;

public record BookListResponse(
        Long id,
        String isbn,
        String coverUrl,
        String title,
        Category category,
        Publisher publisher,
        Language language,
        Copies copies,
        Status status) {

    public record Category(
            Long id,
            String name) {
    }

    public record Publisher(
            Long id,
            String name) {
    }

    public record Language(
            Long id,
            String code,
            String name) {
    }

    public record Copies(
            Long loaned,
            Long available) {
    }
}
