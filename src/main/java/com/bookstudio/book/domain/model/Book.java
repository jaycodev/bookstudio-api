package com.bookstudio.book.domain.model;

import com.bookstudio.category.domain.model.Category;
import com.bookstudio.copy.domain.model.Copy;
import com.bookstudio.language.domain.model.Language;
import com.bookstudio.publisher.domain.model.Publisher;
import com.bookstudio.shared.domain.model.type.Status;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(unique = true)
    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    private String edition;

    private Integer pages;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String coverUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Copy> copies = new ArrayList<>();
}
