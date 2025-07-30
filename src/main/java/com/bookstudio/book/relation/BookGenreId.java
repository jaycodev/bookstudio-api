package com.bookstudio.book.relation;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookGenreId implements Serializable {

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "genre_id")
    private Long genreId;
}
