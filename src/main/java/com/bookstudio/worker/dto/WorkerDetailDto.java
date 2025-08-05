package com.bookstudio.worker.dto;

import com.bookstudio.role.dto.RoleSummaryDto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDetailDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private RoleSummaryDto role;
    private String profilePhotoUrl;
    private String status;
}
