package com.bookstudio.category.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDetailDto {
    private Long id;
    private String name;
    private String level;
    private String description;
    private String status;
}
