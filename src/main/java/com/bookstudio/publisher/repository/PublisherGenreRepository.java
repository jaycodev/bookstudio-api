package com.bookstudio.publisher.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstudio.genre.dto.GenreSummaryDto;
import com.bookstudio.publisher.model.Publisher;
import com.bookstudio.publisher.relation.PublisherGenre;
import com.bookstudio.publisher.relation.PublisherGenreId;

public interface PublisherGenreRepository extends JpaRepository<PublisherGenre, PublisherGenreId> {
    @Query("""
        SELECT new com.bookstudio.genre.dto.GenreSummaryDto(
            g.genreId,
            g.name
        )
        FROM PublisherGenre pg
        JOIN pg.genre g
        WHERE pg.publisher.publisherId = :publisherId
    """)
    List<GenreSummaryDto> findGenreSummariesByPublisherId(@Param("publisherId") Long publisherId);

    void deleteAllByPublisher(Publisher publisher);
}
