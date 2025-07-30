package com.bookstudio.shared.model;

import java.io.Serializable;

import com.bookstudio.author.model.Author;
import com.bookstudio.book.model.Book;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_authors", schema = "bookstudio_db")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookAuthor {
    
    @EmbeddedId
    private BookAuthorId id;

    @ManyToOne
    @MapsId("bookId")
    private Book book;

    @ManyToOne
    @MapsId("authorId")
    private Author author;
}

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookAuthorId implements Serializable {
    private Long bookId;
    private Long authorId;
}
