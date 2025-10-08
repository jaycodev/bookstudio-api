package com.bookstudio.book.application.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.bookstudio.shared.domain.model.type.Status;

public record UpdateBookRequest(
    String title,
    String isbn,
    Long languageId,
    String edition,
    Integer pages,
    String description,
    String coverUrl,
    Long publisherId,
    Long categoryId,
    LocalDate releaseDate,
    Status status,
    List<Long> authorIds,
    List<Long> genreIds
) {}
