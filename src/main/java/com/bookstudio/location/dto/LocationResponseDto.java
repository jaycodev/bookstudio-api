package com.bookstudio.location.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationResponseDto {
    private Long locationId;
    private String name;
    private String description;
}
