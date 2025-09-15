package com.bookstudio.category.domain.dto.request;

import com.bookstudio.category.domain.model.CategoryLevel;
import com.bookstudio.shared.domain.model.Status;

import lombok.Data;

@Data
public class UpdateCategoryRequest {
    private String name;
    private CategoryLevel level;
    private String description;
    private Status status;
}
