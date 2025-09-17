package com.bookstudio.worker.domain.dto.response;

import com.bookstudio.worker.domain.model.WorkerStatus;

public record WorkerListResponse(
        Long id,
        String profilePhotoUrl,
        String username,
        String email,
        String fullName,
        Role role,
        WorkerStatus status) {

    public record Role(
            String name) {
    }
}
