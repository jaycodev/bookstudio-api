package com.bookstudio.location.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationSummaryDto {
    private Long id;
    private String name;
}
