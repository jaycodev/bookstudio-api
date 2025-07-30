package com.bookstudio.worker.dto;

import com.bookstudio.role.model.Role;
import com.bookstudio.shared.enums.Status;

import lombok.Data;

@Data
public class CreateWorkerDto {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Role role;
    private String profilePhotoUrl;
    private Status status;
}
