package com.bookstudio.author.dto;

import java.time.LocalDate;

import com.bookstudio.shared.enums.Status;

public record AuthorListDto(
    Long id,
    String photoUrl,
    String name,
    String nationalityName,
    LocalDate birthDate,
    Status status
) {}
