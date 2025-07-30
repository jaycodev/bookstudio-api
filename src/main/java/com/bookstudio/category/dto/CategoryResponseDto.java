package com.bookstudio.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryResponseDto {
    private Long categoryId;
    private String name;
    private String level;
    private String description;
    private String status;
}