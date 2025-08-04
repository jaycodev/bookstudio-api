package com.bookstudio.role.service;

import org.springframework.stereotype.Service;

import com.bookstudio.role.dto.PermissionSummaryDto;
import com.bookstudio.role.relation.Permission;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionService {
    public PermissionSummaryDto toSummaryDto(Permission permission) {
        return PermissionSummaryDto.builder()
                .id(permission.getPermissionId())
                .code(permission.getCode())
                .description(permission.getDescription())
                .build();
    }
}
