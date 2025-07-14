package com.bookstudio.shared.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GenreID")
    private Long genreId;

    @Column(name = "Name", nullable = false, unique = true, length = 255)
    private String name;
}
