package com.bookstudio.category.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategorySummaryDto {
    private Long id;
    private String name;
}
