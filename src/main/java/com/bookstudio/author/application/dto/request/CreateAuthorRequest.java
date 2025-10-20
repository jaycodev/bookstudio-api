package com.bookstudio.author.application.dto.request;

import java.time.LocalDate;

import com.bookstudio.shared.type.Status;

public record CreateAuthorRequest(
    String name,
    Long nationalityId,
    LocalDate birthDate,
    String biography,
    Status status,
    String photoUrl
) {}
