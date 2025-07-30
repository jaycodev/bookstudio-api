package com.bookstudio.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstudio.book.model.Book;
import com.bookstudio.book.relation.BookGenre;
import com.bookstudio.book.relation.BookGenreId;
import com.bookstudio.genre.dto.GenreDto;

public interface BookGenreRepository extends JpaRepository<BookGenre, BookGenreId> {
    @Query("""
        SELECT new com.bookstudio.genre.dto.GenreDto(
            g.genreId,
            g.name
        )
        FROM BookGenre bg
        JOIN bg.genre g
        WHERE bg.book.bookId = :bookId
    """)
    List<GenreDto> findGenreDtosByBookId(@Param("bookId") Long bookId);

    void deleteAllByBook(Book book);
}
