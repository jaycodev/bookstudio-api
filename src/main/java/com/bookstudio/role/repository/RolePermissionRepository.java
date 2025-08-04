package com.bookstudio.role.repository;

import com.bookstudio.role.dto.PermissionSummaryDto;
import com.bookstudio.role.model.Role;
import com.bookstudio.role.relation.RolePermission;
import com.bookstudio.role.relation.RolePermissionId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
    @Query("""
        SELECT new com.bookstudio.role.dto.PermissionSummaryDto(
            p.permissionId,
            p.code,
            p.description
        )
        FROM RolePermission rp
        JOIN rp.permission p
        WHERE rp.role.roleId = :roleId
    """)
    List<PermissionSummaryDto> findPermissionSummariesByRoleId(@Param("roleId") Long roleId);

    void deleteAllByRole(Role role);
}
