package com.bookstudio.location.dto;

import lombok.Data;

@Data
public class UpdateLocationDto {
    private Long locationId;
    private String name;
    private String description;
}
