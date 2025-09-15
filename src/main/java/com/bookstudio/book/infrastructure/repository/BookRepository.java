package com.bookstudio.book.infrastructure.repository;

import com.bookstudio.book.domain.dto.response.BookDetailResponse;
import com.bookstudio.book.domain.dto.response.BookListResponse;
import com.bookstudio.book.domain.model.Book;
import com.bookstudio.shared.domain.dto.response.OptionResponse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("""
        SELECT new com.bookstudio.book.domain.dto.response.BookListResponse(
            b.id,
            b.isbn,
            b.coverUrl,
            b.title,
            new com.bookstudio.book.domain.dto.response.BookListResponse$Category(
                c.id,
                c.name
            ),
            new com.bookstudio.book.domain.dto.response.BookListResponse$Publisher(
                p.id,
                p.name
            ),
            new com.bookstudio.book.domain.dto.response.BookListResponse$Language(
                l.id,
                l.code,
                l.name
            ),
            new com.bookstudio.book.domain.dto.response.BookListResponse$Copies(
                COALESCE(SUM(CASE WHEN cpy.status <> com.bookstudio.copy.domain.model.CopyStatus.DISPONIBLE THEN 1 ELSE 0 END), 0),
                COALESCE(SUM(CASE WHEN cpy.status =  com.bookstudio.copy.domain.model.CopyStatus.DISPONIBLE THEN 1 ELSE 0 END), 0)
            ),
            b.status
        )
        FROM Book b
        JOIN b.publisher p
        JOIN b.category c
        JOIN b.language l
        LEFT JOIN b.copies cpy
        GROUP BY b.id, b.isbn, b.coverUrl, b.title, c.id, c.name, p.id, p.name, l.id, l.code, l.name, b.status
        ORDER BY b.id DESC
    """)
    List<BookListResponse> findList();

    @Query("""
        SELECT 
            b.id AS value,
            b.title AS label
        FROM Book b
        WHERE b.status = com.bookstudio.shared.domain.model.Status.ACTIVO
        ORDER BY b.title ASC
    """)
    List<OptionResponse> findForOptions();

    @Query("""
        SELECT new com.bookstudio.book.domain.dto.response.BookDetailResponse(
            b.id,
            b.title,
            b.isbn,
            new com.bookstudio.book.domain.dto.response.BookDetailResponse$Language(
                l.id,
                l.code,
                l.name
            ),
            b.edition,
            b.pages,
            b.description,
            b.coverUrl,
            new com.bookstudio.book.domain.dto.response.BookDetailResponse$Publisher(
                p.id,
                p.name
            ),
            new com.bookstudio.book.domain.dto.response.BookDetailResponse$Category(
                c.id,
                c.name
            ),
            b.releaseDate,
            b.status,
            NULL,
            NULL
        )
        FROM Book b
        JOIN b.publisher p
        JOIN b.category c
        JOIN b.language l
        WHERE b.id = :id
    """)
    Optional<BookDetailResponse> findDetailById(Long id);

    @Query("""
        SELECT new com.bookstudio.book.domain.dto.response.BookListResponse$Copies(
            COALESCE(SUM(CASE WHEN c.status <> com.bookstudio.copy.domain.model.CopyStatus.DISPONIBLE THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN c.status = com.bookstudio.copy.domain.model.CopyStatus.DISPONIBLE THEN 1 ELSE 0 END), 0)
        )
        FROM Copy c
        WHERE c.book.id = :id
    """)
    BookListResponse.Copies findCopyCountsById(Long id);
}
