package com.bookstudio.category.dto;

import com.bookstudio.category.model.CategoryLevel;
import com.bookstudio.shared.enums.Status;

public record CategoryListDto(
    Long id,
    String name,
    CategoryLevel level,
    String description,
    Status status
) {}
