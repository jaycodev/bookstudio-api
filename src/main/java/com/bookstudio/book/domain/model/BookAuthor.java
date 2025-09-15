package com.bookstudio.book.domain.model;


import com.bookstudio.author.domain.model.Author;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookAuthor {

    @EmbeddedId
    private BookAuthorId id;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @MapsId("authorId")
    @JoinColumn(name = "author_id")
    private Author author;
}
