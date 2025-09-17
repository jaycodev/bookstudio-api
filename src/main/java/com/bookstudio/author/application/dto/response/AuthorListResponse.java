package com.bookstudio.author.application.dto.response;

import java.time.LocalDate;

import com.bookstudio.shared.domain.model.type.Status;

public record AuthorListResponse(
        Long id,
        String photoUrl,
        String name,
        Nationality nationality,
        LocalDate birthDate,
        Status status) {

    public record Nationality(
            Long id,
            String code,
            String name) {
    }
}
