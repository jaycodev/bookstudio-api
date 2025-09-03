package com.bookstudio.worker.dto;

import com.bookstudio.worker.model.WorkerStatus;

public record WorkerListDto(
    Long id,
    String profilePhotoUrl,
    String username,
    String email,
    String firstName,
    String lastName,
    String roleName,
    WorkerStatus status
) {}
