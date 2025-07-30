package com.bookstudio.publisher.relation;

import com.bookstudio.publisher.model.Publisher;
import com.bookstudio.shared.model.Genre;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "publisher_genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublisherGenre {
    
    @EmbeddedId
    private PublisherGenreId id;

    @ManyToOne
    @MapsId("publisherId")
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToOne
    @MapsId("genreId")
    @JoinColumn(name = "genre_id")
    private Genre genre;
}
