package com.bookstudio.book.dto;

import com.bookstudio.shared.enums.Status;

public record BookListDto(
    Long id,
    String isbn,
    String coverUrl,
    String title,
    Long categoryId,
    String categoryName,
    Long publisherId,
    String publisherName,
    Long languageId,
    String languageCode,
    String languageName,
    Long loanedCopies,
    Long availableCopies,
    Status status
) {}
