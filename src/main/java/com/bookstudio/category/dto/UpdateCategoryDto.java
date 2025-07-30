package com.bookstudio.category.dto;

import com.bookstudio.shared.enums.CategoryLevel;
import com.bookstudio.shared.enums.Status;

import lombok.Data;

@Data
public class UpdateCategoryDto {
    private Long categoryId;
    private String name;
    private CategoryLevel level;
    private String description;
    private Status status;
}
