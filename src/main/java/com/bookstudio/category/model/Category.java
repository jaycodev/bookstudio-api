package com.bookstudio.category.model;

import com.bookstudio.shared.enums.CategoryLevel;
import com.bookstudio.shared.enums.Status;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories", schema = "bookstudio_db")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryLevel level;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;
}
