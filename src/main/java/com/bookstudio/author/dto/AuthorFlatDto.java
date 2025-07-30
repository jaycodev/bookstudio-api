package com.bookstudio.author.dto;

import java.time.LocalDate;

import com.bookstudio.shared.enums.Status;

public record AuthorFlatDto(
    Long id,
    String name,
    String biography,
    LocalDate birthDate,
    String photoUrl,
    Status status,
    Long nationalityId,
    String nationalityName
) {}
