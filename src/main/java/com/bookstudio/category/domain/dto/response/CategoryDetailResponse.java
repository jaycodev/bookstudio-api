package com.bookstudio.category.domain.dto.response;

import com.bookstudio.category.domain.model.type.CategoryLevel;
import com.bookstudio.shared.domain.model.type.Status;

public record CategoryDetailResponse(
        Long id,
        String name,
        CategoryLevel level,
        String description,
        Status status) {
}
