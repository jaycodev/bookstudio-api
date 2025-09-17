package com.bookstudio.worker.application.dto.request;


import com.bookstudio.worker.domain.model.type.WorkerStatus;

import lombok.Data;

@Data
public class CreateWorkerRequest {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Long roleId;
    private String profilePhotoUrl;
    private WorkerStatus status;
}
