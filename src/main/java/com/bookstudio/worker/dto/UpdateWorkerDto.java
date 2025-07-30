package com.bookstudio.worker.dto;

import com.bookstudio.role.model.Role;
import com.bookstudio.shared.enums.Status;

import lombok.Data;

@Data
public class UpdateWorkerDto {
    private Long workerId;
    private String firstName;
    private String lastName;
    private Role role;
    private boolean deletePhoto;
    private String profilePhotoUrl;
    private Status status;
}
