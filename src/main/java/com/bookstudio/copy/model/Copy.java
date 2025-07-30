package com.bookstudio.copy.model;

import com.bookstudio.book.model.Book;
import com.bookstudio.location.model.Shelf;
import com.bookstudio.shared.enums.Condition;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "copies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Copy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long copyId;

    @Column(insertable = false, updatable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "shelf_id", nullable = false)
    private Shelf shelf;

    @Column(nullable = false, unique = true)
    private String barcode;

    private Boolean isAvailable;

    @Enumerated(EnumType.STRING)
    private Condition condition;
}
