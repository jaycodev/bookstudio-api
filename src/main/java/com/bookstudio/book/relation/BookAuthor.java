package com.bookstudio.book.relation;

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
