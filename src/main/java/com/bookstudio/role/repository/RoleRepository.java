package com.bookstudio.role.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstudio.role.model.Role;
import com.bookstudio.role.projection.RoleSelectProjection;
import com.bookstudio.role.projection.RoleViewProjection;

public interface RoleRepository extends JpaRepository<Role, Long>{
    @Query("""
        SELECT 
            r.roleId AS roleId,
            r.name AS name,
            r.description AS description
        FROM Role r
        ORDER BY r.roleId DESC
    """)
    List<RoleViewProjection> findList();

    @Query("""
        SELECT 
            r.roleId AS roleId,
            r.name AS name,
            r.description AS description
        FROM Role r
        WHERE r.roleId = :id
    """)
    Optional<RoleViewProjection> findInfoById(@Param("id") Long id);

    @Query("""
        SELECT 
            r.roleId AS roleId,
            r.name AS name
        FROM Role r
        ORDER BY r.name ASC
    """)
    List<RoleSelectProjection> findSelectList();
}
