package com.bookstudio.category.dto;

import com.bookstudio.shared.enums.CategoryLevel;
import com.bookstudio.shared.enums.Status;

public record CategoryListDto(
    String name,
    CategoryLevel level,
    String description,
    Status status,
    Long id
) {}
