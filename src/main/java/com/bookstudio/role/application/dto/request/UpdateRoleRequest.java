package com.bookstudio.role.application.dto.request;

import java.util.List;

import org.springframework.lang.NonNull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UpdateRoleRequest(
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must not exceed 50 characters")
    String name,

    String description,

    @NonNull
    @NotEmpty(message = "Permission IDs cannot be empty")
    List<Long> permissionIds
) {}
