package com.bookstudio.role.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions", schema = "bookstudio_db")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;
}
