package com.bookstudio.category.domain.model;

import com.bookstudio.shared.domain.model.Status;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryLevel level;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;
}
