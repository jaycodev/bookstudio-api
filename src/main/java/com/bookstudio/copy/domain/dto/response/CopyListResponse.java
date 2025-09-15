package com.bookstudio.copy.domain.dto.response;

import com.bookstudio.copy.domain.model.CopyCondition;
import com.bookstudio.copy.domain.model.CopyStatus;

public record CopyListResponse(
        Long id,
        String code,
        Book book,
        Shelf shelf,
        Location location,
        CopyStatus status,
        CopyCondition condition) {

    public record Book(
            Long id,
            String coverUrl,
            String title) {
    }

    public record Shelf(
            String code,
            String floor) {
    }

    public record Location(
            String name) {
    }
}
