package com.bookstudio.book.dto;

import com.bookstudio.shared.enums.Status;

public record BookListDto(
    Long id,
    String isbn,
    String coverUrl,
    String title,
    String categoryName,
    String publisherName,
    String languageCode,
    String languageName,
    Long loanedCopies,
    Long availableCopies,
    Status status
) {}
