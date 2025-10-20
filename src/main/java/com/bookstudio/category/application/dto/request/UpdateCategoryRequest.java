package com.bookstudio.category.application.dto.request;

import com.bookstudio.category.domain.model.type.CategoryLevel;
import com.bookstudio.shared.type.Status;

public record UpdateCategoryRequest(
    String name,
    CategoryLevel level,
    String description,
    Status status
) {}
