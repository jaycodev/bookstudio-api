package com.bookstudio.publisher.domain.model;

import com.bookstudio.nationality.domain.model.Nationality;
import com.bookstudio.shared.domain.model.type.Status;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "publishers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Publisher {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nationality_id", nullable = false)
    private Nationality nationality;

    @Column(name = "foundation_year", nullable = false)
    private Integer foundationYear;

    private String website;

    private String address;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(length = 512)
    private String photoUrl;
}
