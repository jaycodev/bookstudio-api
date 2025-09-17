package com.bookstudio.copy.domain.model;

import com.bookstudio.book.domain.model.Book;
import com.bookstudio.copy.domain.model.type.CopyCondition;
import com.bookstudio.copy.domain.model.type.CopyStatus;
import com.bookstudio.location.domain.model.Shelf;

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
    private Long id;

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

    @Enumerated(EnumType.STRING)
    private CopyStatus status;

    @Enumerated(EnumType.STRING)
    private CopyCondition condition;
}
