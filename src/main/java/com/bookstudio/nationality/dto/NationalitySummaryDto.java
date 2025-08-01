package com.bookstudio.nationality.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NationalitySummaryDto {
    private Long id;
    private String name;
}
