package com.bookstudio.worker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkerResponseDto {
    private Long workerId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String profilePhotoUrl;
    private String status;
}
