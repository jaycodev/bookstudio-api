package com.bookstudio.location.domain.dto.request;

import lombok.Data;

@Data
public class CreateShelfRequest {
    private String code;
    private String floor;
    private String description;
}
