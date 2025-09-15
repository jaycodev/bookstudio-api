package com.bookstudio.publisher.domain.dto.response;

import com.bookstudio.shared.domain.model.Status;

public record PublisherListResponse(
        Long id,
        String photoUrl,
        String name,
        Nationality nationality,
        String website,
        String address,
        Status status) {

    public record Nationality(
            Long id,
            String code,
            String name) {
    }
}