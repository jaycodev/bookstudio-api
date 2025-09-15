package com.bookstudio.location.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shelves")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shelf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private String code;

    private String floor;

    @Column(columnDefinition = "TEXT")
    private String description;
}