package com.bookstudio.worker.application.dto.response;

import com.bookstudio.worker.domain.model.type.WorkerStatus;

public record WorkerDetailResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        Role role,
        String profilePhotoUrl,
        WorkerStatus status) {

    public record Role(
            Long id,
            String name) {
    }
}
