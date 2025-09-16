package com.bookstudio.location.domain.model;

import java.util.ArrayList;
import java.util.List;

import com.bookstudio.copy.domain.model.Copy;

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

    @OneToMany(mappedBy = "shelf", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Copy> copies = new ArrayList<>();
}
