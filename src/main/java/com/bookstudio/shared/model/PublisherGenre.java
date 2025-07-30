package com.bookstudio.shared.model;

import java.io.Serializable;

import com.bookstudio.publisher.model.Publisher;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "publisher_genres", schema = "bookstudio_db")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublisherGenre {
    
    @EmbeddedId
    private PublisherGenreId id;

    @ManyToOne
    @MapsId("publisherId")
    private Publisher publisher;

    @ManyToOne
    @MapsId("genreId")
    private Genre genre;
}

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublisherGenreId implements Serializable {
    private Long publisherId;
    private Long genreId;
}