package com.bookstudio.location.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShelfDto {
    private Long id;
    private LocationDto location;
    private String code;
    private String floor;
    private String description;
}
