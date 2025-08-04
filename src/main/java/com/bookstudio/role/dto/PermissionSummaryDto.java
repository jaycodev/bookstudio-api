package com.bookstudio.role.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionSummaryDto {
    private Long id;
    private String code;
    private String description;
}
