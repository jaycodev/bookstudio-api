package com.bookstudio.worker.domain.dto.response;

import com.bookstudio.worker.domain.model.type.WorkerStatus;

public record WorkerListResponse(
        Long id,
        String profilePhotoUrl,
        String username,
        String email,
        String fullName,
        Role role,
        WorkerStatus status) {

    public record Role(
            Long id,
            String name) {
    }
}
