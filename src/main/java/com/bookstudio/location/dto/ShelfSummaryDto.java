package com.bookstudio.location.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShelfSummaryDto {
    private Long id;
    private String code;
    private String floor;
    private String description;
}
