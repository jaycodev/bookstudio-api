package com.bookstudio.publisher.relation;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublisherGenreId implements Serializable {

    @Column(name = "publisher_id")
    private Long publisherId;

    @Column(name = "genre_id")
    private Long genreId;
}