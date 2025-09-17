package com.bookstudio.author.application.dto.response;

import java.time.LocalDate;

import com.bookstudio.shared.domain.model.type.Status;

public record AuthorDetailResponse(
        Long id,
        String name,
        Nationality nationality,
        LocalDate birthDate,
        String biography,
        Status status,
        String photoUrl) {

    public record Nationality(
            Long id,
            String code,
            String name) {
    }
}
