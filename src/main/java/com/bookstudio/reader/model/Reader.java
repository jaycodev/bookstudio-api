package com.bookstudio.reader.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "readers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long readerId;

    @Column(insertable = false, updatable = false)
    private String code;

    @Column(nullable = false, unique = true, length = 8)
    private String dni;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 9)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private ReaderGender gender;

    @Enumerated(EnumType.STRING)
    private ReaderType type;

    @Enumerated(EnumType.STRING)
    private ReaderStatus status;

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }
}