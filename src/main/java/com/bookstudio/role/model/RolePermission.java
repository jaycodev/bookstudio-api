package com.bookstudio.role.model;

import java.io.Serializable;

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

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionId implements Serializable {
    private Long roleId;
    private Long permissionId;
}
