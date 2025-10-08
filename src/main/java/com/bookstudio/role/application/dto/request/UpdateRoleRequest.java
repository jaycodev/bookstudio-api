package com.bookstudio.role.application.dto.request;

import java.util.List;

public record UpdateRoleRequest(
    String name,
    String description,
    List<Long> permissionIds
) {}
