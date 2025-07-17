package com.bookstudio.auth.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "PasswordResetTokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TokenID")
    private Long id;

    @Column(name = "Email", nullable = false, length = 100)
    private String email;

    @Column(name = "Token", nullable = false, unique = true, length = 255)
    private String token;

    @Column(name = "ExpiryTime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryTime;

    @Column(name = "CreatedAt", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "Used", nullable = false)
    private boolean used;
}
