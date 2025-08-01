package com.bookstudio.location.dto;

import java.util.List;

import lombok.Data;

@Data
public class CreateLocationDto {
    private String name;
    private String description;
    private List<CreateShelfDto> shelves;
}
