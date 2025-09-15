package com.bookstudio.publisher.domain.dto.response;

import java.util.List;

import com.bookstudio.shared.domain.model.Status;

public record PublisherDetailResponse(
        Long id,
        String name,
        Nationality nationality,
        Integer foundationYear,
        String website,
        String address,
        Status status,
        String photoUrl,
        List<GenreItem> genres) {

    public PublisherDetailResponse withGenres(List<GenreItem> genres) {
        return new PublisherDetailResponse(
                id, name, nationality, foundationYear, website, address, status, photoUrl, genres);
    }

    public record Nationality(
            Long id,
            String code,
            String name) {
    }

    public record GenreItem(
            Long id,
            String name) {
    }
}
