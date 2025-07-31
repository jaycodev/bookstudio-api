package com.bookstudio.location.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    private Long id;
    private String name;
    private String description;
}
