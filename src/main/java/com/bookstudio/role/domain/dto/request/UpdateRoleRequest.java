package com.bookstudio.role.domain.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class UpdateRoleRequest {
    private String name;
    private String description;
    private List<Long> permissionIds;
}
