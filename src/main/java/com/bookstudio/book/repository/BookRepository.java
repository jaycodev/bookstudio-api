package com.bookstudio.book.repository;

import com.bookstudio.book.dto.BookListDto;
import com.bookstudio.book.dto.BookSelectDto;
import com.bookstudio.book.model.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("""
        SELECT new com.bookstudio.book.dto.BookListDto(
            b.coverUrl,
            b.title,
            c.name,
            p.name,
            l.code,
            b.releaseDate,
            b.status,
            b.isbn,
            b.bookId
        )
        FROM Book b
        JOIN b.publisher p
        JOIN b.category c
        JOIN b.language l
        ORDER BY b.bookId DESC
    """)
    List<BookListDto> findList();

    @Query("""
        SELECT 
            new com.bookstudio.book.dto.BookSelectDto(
                b.bookId, 
                b.title
            )
        FROM Book b 
        WHERE b.status = 'activo'
    """)
    List<BookSelectDto> findForSelect();
}
