package com.bookstudio.worker.application.dto.request;

import com.bookstudio.worker.domain.model.type.WorkerStatus;

public record UpdateWorkerRequest(
    String firstName,
    String lastName,
    Long roleId,
    String profilePhotoUrl,
    WorkerStatus status
) {}
