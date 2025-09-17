package com.bookstudio.location.application.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class UpdateLocationRequest {
    private String name;
    private String description;
    private List<UpdateShelfRequest> shelves;
}
