package com.bookstudio.role.application.dto.request;

import java.util.List;

public record CreateRoleRequest(
    String name,
    String description,
    List<Long> permissionIds
) {}
