package com.bookstudio.book.relation;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookGenreId implements Serializable {
    private Long bookId;
    private Long genreId;
}
