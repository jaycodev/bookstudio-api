package com.bookstudio.book.domain.model;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookAuthorId implements Serializable {

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "author_id")
    private Long authorId;
}
