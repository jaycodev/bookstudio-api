package com.bookstudio.book.model;

import com.bookstudio.category.model.Category;
import com.bookstudio.publisher.model.Publisher;
import com.bookstudio.shared.enums.Status;
import com.bookstudio.shared.model.Language;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "books", schema = "bookstudio_db")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

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
}
