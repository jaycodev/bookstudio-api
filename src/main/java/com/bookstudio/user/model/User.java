package com.bookstudio.user.model;

import com.bookstudio.shared.enums.Role;
import com.bookstudio.shared.util.IdFormatter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Long id;

    @Column(name = "Username", nullable = false, unique = true, length = 50, columnDefinition = "VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin")
    private String username;

    @Column(name = "Email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "FirstName", nullable = false)
    private String firstName;

    @Column(name = "LastName", nullable = false)
    private String lastName;

    @Column(name = "Password", nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role", nullable = false, columnDefinition = "ENUM('administrador', 'bibliotecario')")
    private Role role;

    @Lob
    @Column(name = "ProfilePhoto")
    private byte[] profilePhoto;

    @Transient
    public String getFormattedId() {
        return IdFormatter.formatId(String.valueOf(id), "U");
    }

    @Transient
    private String profilePhotoBase64;
}
