package com.bookstudio.worker.domain.dto.request;

import com.bookstudio.worker.domain.model.type.WorkerStatus;

import lombok.Data;

@Data
public class UpdateWorkerRequest {
    private String firstName;
    private String lastName;
    private Long roleId;
    private String profilePhotoUrl;
    private WorkerStatus status;
}
