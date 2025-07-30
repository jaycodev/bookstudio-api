package com.bookstudio.role.relation;

import com.bookstudio.role.model.Role;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_permissions", schema = "bookstudio_db")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermission {

    @EmbeddedId
    private RolePermissionId id;

    @ManyToOne
    @MapsId("roleId")
    private Role role;

    @ManyToOne
    @MapsId("permissionId")
    private Permission permission;
}
