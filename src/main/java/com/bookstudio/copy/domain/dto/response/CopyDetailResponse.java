package com.bookstudio.copy.domain.dto.response;

import com.bookstudio.copy.domain.model.CopyCondition;
import com.bookstudio.copy.domain.model.CopyStatus;

public record CopyDetailResponse(
        Long id,
        String code,
        Book book,
        Shelf shelf,
        String barcode,
        CopyStatus status,
        CopyCondition condition) {

    public record Book(
            Long id,
            String isbn,
            String coverUrl,
            String title) {
    }

    public record Shelf(
            Long id,
            String code,
            String floor,
            String description) {
    }
}
