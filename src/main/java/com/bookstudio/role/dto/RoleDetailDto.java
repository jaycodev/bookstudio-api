package com.bookstudio.role.dto;

import java.util.List;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDetailDto {
    private Long id;
    private String name;
    private String description;
    private List<PermissionSummaryDto> permissions;
}
