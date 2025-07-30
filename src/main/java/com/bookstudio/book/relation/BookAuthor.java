package com.bookstudio.book.relation;

import com.bookstudio.author.model.Author;
import com.bookstudio.book.model.Book;

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
