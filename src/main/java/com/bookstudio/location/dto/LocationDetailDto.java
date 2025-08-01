package com.bookstudio.location.dto;

import java.util.List;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDetailDto {
    private Long id;
    private String name;
    private String description;
    private List<ShelfSummaryDto> shelves;
}
