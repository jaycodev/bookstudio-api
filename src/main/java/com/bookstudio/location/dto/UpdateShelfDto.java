package com.bookstudio.location.dto;

import lombok.Data;

@Data
public class UpdateShelfDto {
    private Long id;
    private String code;
    private String floor;
    private String description;
}
