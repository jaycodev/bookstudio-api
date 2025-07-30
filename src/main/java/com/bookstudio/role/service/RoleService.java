package com.bookstudio.role.service;

import com.bookstudio.role.model.Role;
import com.bookstudio.role.projection.RoleSelectProjection;
import com.bookstudio.role.projection.RoleViewProjection;
import com.bookstudio.role.repository.RoleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<RoleViewProjection> getList() {
        return roleRepository.findList();
    }

    public Optional<RoleViewProjection> getInfoById(Long roleId) {
        return roleRepository.findInfoById(roleId);
    }

    public Optional<Role> findById(Long roleId) {
        return roleRepository.findById(roleId);
    }

    @Transactional
    public Role create(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public Role update(Role updatedRole) {
        Role existing = roleRepository.findById(updatedRole.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + updatedRole.getRoleId()));

        existing.setName(updatedRole.getName());
        existing.setDescription(updatedRole.getDescription());

        return roleRepository.save(existing);
    }

    public List<RoleSelectProjection> getForSelect() {
        return roleRepository.findSelectList();
    }
}
