package com.bookstudio.worker.dto;

import com.bookstudio.shared.enums.Status;

import lombok.Data;

@Data
public class UpdateWorkerDto {
    private String firstName;
    private String lastName;
    private Long roleId;
    private boolean deletePhoto;
    private String profilePhotoUrl;
    private Status status;
}
