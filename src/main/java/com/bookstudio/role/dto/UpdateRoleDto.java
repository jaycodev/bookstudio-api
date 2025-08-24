package com.bookstudio.role.dto;

import java.util.List;

import lombok.Data;

@Data
public class UpdateRoleDto {
    private String name;
    private String description;
    private List<Long> permissionIds;
}
