package com.bookstudio.book.dto;

import java.time.LocalDate;

import com.bookstudio.shared.enums.Status;

public record BookListDto(
    String isbn,
    String coverUrl,
    String title,
    String categoryName,
    String publisherName,
    String languageCode,
    String languageName,
    LocalDate releaseDate,
    Status status,
    Long id
) {}
