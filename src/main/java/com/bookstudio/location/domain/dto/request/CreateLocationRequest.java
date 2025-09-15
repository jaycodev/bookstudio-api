package com.bookstudio.location.domain.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class CreateLocationRequest {
    private String name;
    private String description;
    private List<CreateShelfRequest> shelves;
}
