package com.bookstudio.user.repository;

import com.bookstudio.user.model.User;
import com.bookstudio.user.projection.UserDetailProjection;
import com.bookstudio.user.projection.UserListProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByIdNot(Long excludedId);

    @Query("""
        SELECT 
            u.id AS userId,
            u.username AS username,
            u.email AS email,
            u.firstName AS firstName,
            u.lastName AS lastName,
            u.role AS role,
            u.profilePhoto AS profilePhoto
        FROM User u
    """)
    List<UserListProjection> findList();

    @Query("""
        SELECT 
            u.id AS userId,
            u.username AS username,
            u.email AS email,
            u.firstName AS firstName,
            u.lastName AS lastName,
            u.role AS role,
            u.profilePhoto AS profilePhoto
        FROM User u
        WHERE u.id = :id
    """)
    Optional<UserDetailProjection> findDetailById(@Param("id") Long id);
}
