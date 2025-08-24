package com.bookstudio.role.service;

import com.bookstudio.role.dto.CreateRoleDto;
import com.bookstudio.role.dto.RoleDetailDto;
import com.bookstudio.role.dto.RoleListDto;
import com.bookstudio.role.dto.RoleSelectDto;
import com.bookstudio.role.dto.RoleSummaryDto;
import com.bookstudio.role.dto.UpdateRoleDto;
import com.bookstudio.role.model.Role;
import com.bookstudio.role.relation.Permission;
import com.bookstudio.role.relation.RolePermission;
import com.bookstudio.role.relation.RolePermissionId;
import com.bookstudio.role.repository.RolePermissionRepository;
import com.bookstudio.role.repository.RoleRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private final RolePermissionRepository rolePermissionRepository;

    public List<RoleListDto> getList() {
        return roleRepository.findList();
    }

    public Optional<Role> findById(Long roleId) {
        return roleRepository.findById(roleId);
    }

    public RoleDetailDto getInfoById(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + roleId));

        return RoleDetailDto.builder()
                .id(role.getRoleId())
                .name(role.getName())
                .description(role.getDescription())
                .permissions(rolePermissionRepository.findPermissionSummariesByRoleId(role.getRoleId()))
                .build();
    }

    @Transactional
    public RoleListDto create(CreateRoleDto dto) {
        Role role = new Role();
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());

        Role saved = roleRepository.save(role);

        if (dto.getPermissionIds() != null) {
            for (Long permissionId : dto.getPermissionIds()) {
                RolePermission relation = RolePermission.builder()
                        .id(new RolePermissionId(saved.getRoleId(), permissionId))
                        .role(saved)
                        .permission(new Permission(permissionId))
                        .build();
                rolePermissionRepository.save(relation);
            }
        }

        return toListDto(saved);
    }

    @Transactional
    public RoleListDto update(Long roleId, UpdateRoleDto dto) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + roleId));

        role.setName(dto.getName());
        role.setDescription(dto.getDescription());

        Role saved = roleRepository.save(role);

        rolePermissionRepository.deleteAllByRole(saved);

        if (dto.getPermissionIds() != null) {
            for (Long permissionId : dto.getPermissionIds()) {
                RolePermission relation = RolePermission.builder()
                        .id(new RolePermissionId(saved.getRoleId(), permissionId))
                        .role(saved)
                        .permission(new Permission(permissionId))
                        .build();
                rolePermissionRepository.save(relation);
            }
        }

        return toListDto(saved);
    }

    public List<RoleSelectDto> getForSelect() {
        return roleRepository.findForSelect();
    }

    public RoleSummaryDto toSummaryDto(Role role) {
        return RoleSummaryDto.builder()
                .id(role.getRoleId())
                .name(role.getName())
                .build();
    }

    private RoleListDto toListDto(Role role) {
        return new RoleListDto(
                role.getName(),
                role.getDescription(),
                role.getRoleId());
    }
}
