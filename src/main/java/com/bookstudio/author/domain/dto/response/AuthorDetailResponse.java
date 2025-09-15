package com.bookstudio.author.domain.dto.response;

import java.time.LocalDate;

import com.bookstudio.shared.domain.model.Status;

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
