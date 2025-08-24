package com.bookstudio.role.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstudio.role.dto.RoleListDto;
import com.bookstudio.role.dto.RoleSelectDto;
import com.bookstudio.role.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
    @Query("""
        SELECT new com.bookstudio.role.dto.RoleListDto(
            r.roleId,
            r.name,
            r.description
        )
        FROM Role r
        ORDER BY r.roleId DESC
    """)
    List<RoleListDto> findList();

    @Query("""
        SELECT new com.bookstudio.role.dto.RoleSelectDto(
            r.roleId,
            r.name
        )
        FROM Role r
        ORDER BY r.name ASC
    """)
    List<RoleSelectDto> findForSelect();
}
