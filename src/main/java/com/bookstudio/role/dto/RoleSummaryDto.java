package com.bookstudio.role.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleSummaryDto {
    private Long id;
    private String name;
}
