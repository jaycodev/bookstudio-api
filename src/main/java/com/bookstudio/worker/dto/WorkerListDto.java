package com.bookstudio.worker.dto;

import com.bookstudio.shared.enums.Status;

public record WorkerListDto(
    String profilePhotoUrl,
    String username,
    String email,
    String firstName,
    String lastName,
    String roleName,
    Status status,
    Long id
) {}
