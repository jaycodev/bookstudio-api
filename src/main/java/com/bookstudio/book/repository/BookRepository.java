package com.bookstudio.book.repository;

import com.bookstudio.book.model.Book;
import com.bookstudio.book.projection.BookInfoProjection;
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
            b.bookId AS bookId,
            b.title AS title,
            b.isbn AS isbn,
            b.coverUrl AS coverUrl,
            p.name AS publisherName,
            c.name AS categoryName,
            b.status AS status
        FROM Book b
        JOIN b.publisher p
        JOIN b.category c
        ORDER BY b.bookId DESC
    """)
    List<BookListProjection> findList();

    @Query("""
        SELECT
            b.bookId AS bookId,
            b.title AS title,
            b.isbn AS isbn,
            b.language.code AS languageCode,
            b.edition AS edition,
            b.pages AS pages,
            b.description AS description,
            b.coverUrl AS coverUrl,
            p.name AS publisherName,
            c.name AS categoryName,
            b.releaseDate AS releaseDate,
            b.status AS status
        FROM Book b
        JOIN b.language l
        JOIN b.publisher p
        JOIN b.category c
        WHERE b.bookId = :id
    """)
    Optional<BookInfoProjection> findInfoById(@Param("id") Long id);

    @Query("""
        SELECT 
            b.bookId AS bookId, 
            b.title AS title
        FROM Book b 
        WHERE b.status = 'activo'
    """)
    List<BookSelectProjection> findForSelect();
}
