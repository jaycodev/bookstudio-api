package com.bookstudio.worker.domain.model;

import com.bookstudio.role.domain.model.Role;
import com.bookstudio.worker.domain.model.type.WorkerStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(length = 512)
    private String profilePhotoUrl;

    @Enumerated(EnumType.STRING)
    private WorkerStatus status;

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }
}