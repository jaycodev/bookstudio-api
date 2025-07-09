package com.bookstudio.book.repository;

import com.bookstudio.book.model.Book;
import com.bookstudio.book.projection.BookDetailProjection;
import com.bookstudio.book.projection.BookListProjection;
import com.bookstudio.book.projection.BookSelectProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("""
        SELECT
            b.id AS bookId,
            b.title AS title,
            (b.totalCopies - b.loanedCopies) AS availableCopies,
            b.loanedCopies AS loanedCopies,

            a.id AS authorId,
            a.name AS authorName,
            
            p.id AS publisherId,
            p.name AS publisherName,
            
            b.status AS status
        FROM Book b
        JOIN b.author a
        JOIN b.publisher p
    """)
    List<BookListProjection> findList();

    @Query("""
        SELECT
            b.id AS bookId,
            b.title AS title,
            b.totalCopies AS totalCopies,
            (b.totalCopies - b.loanedCopies) as availableCopies,
            b.loanedCopies AS loanedCopies,
            b.releaseDate AS releaseDate,
            b.status AS status,

            a.id AS authorId,
            a.name AS authorName,

            p.id AS publisherId,
            p.name AS publisherName,

            c.id AS courseId,
            c.name AS courseName,

            g.id AS genreId,
            g.name AS genreName
        FROM Book b
        JOIN b.author a
        JOIN b.publisher p
        JOIN b.course c
        JOIN b.genre g
        WHERE b.id = :id
    """)
    Optional<BookDetailProjection> findDetailById(@Param("id") Long id);

    @Query("SELECT b.id AS bookId, b.title AS title FROM Book b WHERE b.status = 'activo' AND b.totalCopies > b.loanedCopies")
    List<BookSelectProjection> findForSelect();
}
