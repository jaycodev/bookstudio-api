package com.bookstudio.worker.application.dto.request;


import com.bookstudio.worker.domain.model.type.WorkerStatus;

public record CreateWorkerRequest(
    String username,
    String email,
    String firstName,
    String lastName,
    String password,
    Long roleId,
    String profilePhotoUrl,
    WorkerStatus status
) {}
