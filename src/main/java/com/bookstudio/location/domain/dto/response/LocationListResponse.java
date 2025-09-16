package com.bookstudio.location.domain.dto.response;

public record LocationListResponse(
        Long id,
        String name,
        String description,
        Long shelfCount,
        Long copyCount) {
}
