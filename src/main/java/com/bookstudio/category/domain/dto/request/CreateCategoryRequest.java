package com.bookstudio.category.domain.dto.request;

import com.bookstudio.category.domain.model.type.CategoryLevel;
import com.bookstudio.shared.domain.model.type.Status;

import lombok.Data;

@Data
public class CreateCategoryRequest {
    private String name;
    private CategoryLevel level;
    private String description;
    private Status status;
}
