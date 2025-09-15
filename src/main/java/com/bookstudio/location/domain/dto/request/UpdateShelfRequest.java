package com.bookstudio.location.domain.dto.request;

import lombok.Data;

@Data
public class UpdateShelfRequest {
    private Long id;
    private String code;
    private String floor;
    private String description;
}
