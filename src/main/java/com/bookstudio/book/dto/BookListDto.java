package com.bookstudio.book.dto;

import java.time.LocalDate;

import com.bookstudio.shared.enums.Status;

public record BookListDto(
    String coverUrl,
    String title,
    String categoryName,
    String publisherName,
    String languageCode,
    LocalDate releaseDate,
    Status status,
    String isbn,
    Long id
) {}
