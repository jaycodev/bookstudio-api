package com.bookstudio.author.application.dto.request;

import java.time.LocalDate;

import com.bookstudio.shared.domain.model.type.Status;

public record UpdateAuthorRequest(
    String name,
    Long nationalityId,
    LocalDate birthDate,
    String biography,
    Status status,
    String photoUrl
) {}
