package com.bookstudio.book.model;

import java.io.Serializable;

import com.bookstudio.shared.model.Genre;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_genres", schema = "bookstudio_db")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookGenre {
    
    @EmbeddedId
    private BookGenreId id;

    @ManyToOne
    @MapsId("bookId")
    private Book book;

    @ManyToOne
    @MapsId("genreId")
    private Genre genre;
}

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookGenreId implements Serializable {
    private Long bookId;
    private Long genreId;
}