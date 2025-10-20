package com.bookstudio.publisher.domain.model;

import com.bookstudio.genre.domain.model.Genre;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "publisher_genres")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
