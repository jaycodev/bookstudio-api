package com.bookstudio.book.domain.model;

import com.bookstudio.genre.domain.model.Genre;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookGenre {

    @EmbeddedId
    private BookGenreId id;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @MapsId("genreId")
    @JoinColumn(name = "genre_id")
    private Genre genre;
}
