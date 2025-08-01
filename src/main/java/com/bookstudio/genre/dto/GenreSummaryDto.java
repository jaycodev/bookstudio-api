package com.bookstudio.genre.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenreSummaryDto {
    private Long id;
    private String name;
}
