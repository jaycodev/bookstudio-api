package com.bookstudio.category.dto;

import com.bookstudio.category.model.CategoryLevel;
import com.bookstudio.shared.enums.Status;

import lombok.Data;

@Data
public class UpdateCategoryDto {
    private String name;
    private CategoryLevel level;
    private String description;
    private Status status;
}
