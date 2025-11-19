package com.bookstudio.location.application.dto.request;

import java.util.List;

import org.springframework.lang.NonNull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UpdateLocationRequest(
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    String name,

    String description,

    @NonNull
    @NotEmpty(message = "Shelves cannot be empty")
    List<UpdateShelfRequest> shelves
) {}
