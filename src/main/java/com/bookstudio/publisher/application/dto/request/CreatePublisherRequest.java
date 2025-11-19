package com.bookstudio.publisher.application.dto.request;

import java.util.List;

import org.springframework.lang.NonNull;

import com.bookstudio.shared.type.Status;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreatePublisherRequest(
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    String name,

    @NonNull
    @NotNull(message = "Nationality ID is required")
    @Min(value = 1, message = "Nationality ID must be at least 1")
    Long nationalityId,

    @NotNull(message = "Foundation year is required")
    @Min(value = 1400, message = "Foundation year must be at least 1400")
    Integer foundationYear,

    @Size(max = 255, message = "Website must not exceed 255 characters")
    String website,

    @Size(max = 255, message = "Address must not exceed 255 characters")
    String address,

    @NotNull(message = "Status is required")
    Status status,

    @Size(max = 1024, message = "Photo URL must not exceed 1024 characters")
    String photoUrl,

    @NonNull
    @NotEmpty(message = "Genre IDs cannot be empty")
    List<Long> genreIds
) {}
