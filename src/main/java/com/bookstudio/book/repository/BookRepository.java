package com.bookstudio.book.repository;

import com.bookstudio.book.dto.BookListDto;
import com.bookstudio.book.dto.BookOptionDto;
import com.bookstudio.book.model.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("""
        SELECT new com.bookstudio.book.dto.BookListDto(
            b.bookId,
            b.isbn,
            b.coverUrl,
            b.title,
            c.categoryId,
            c.name,
            p.publisherId,
            p.name,
            l.languageId,
            l.code,
            l.name,
            (SELECT COUNT(cpy) FROM Copy cpy WHERE cpy.book = b AND cpy.status <> com.bookstudio.copy.model.CopyStatus.DISPONIBLE),
            (SELECT COUNT(cpy) FROM Copy cpy WHERE cpy.book = b AND cpy.status = com.bookstudio.copy.model.CopyStatus.DISPONIBLE),
            b.status
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
            new com.bookstudio.book.dto.BookOptionDto(
                b.bookId, 
                b.title
            )
        FROM Book b 
        WHERE b.status = com.bookstudio.shared.enums.Status.ACTIVO
        ORDER BY b.title ASC
    """)
    List<BookOptionDto> findForOptions();
}
