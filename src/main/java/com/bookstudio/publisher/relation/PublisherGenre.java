package com.bookstudio.publisher.relation;

import com.bookstudio.publisher.model.Publisher;
import com.bookstudio.shared.model.Genre;

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
