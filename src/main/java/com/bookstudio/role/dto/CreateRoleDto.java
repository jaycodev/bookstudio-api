package com.bookstudio.role.dto;

import java.util.List;

import lombok.Data;

@Data
public class CreateRoleDto {
    private String name;
    private String description;
    private List<Long> permissionIds;
}
