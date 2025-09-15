package com.bookstudio.worker.domain.dto.request;


import com.bookstudio.worker.domain.model.WorkerStatus;

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
