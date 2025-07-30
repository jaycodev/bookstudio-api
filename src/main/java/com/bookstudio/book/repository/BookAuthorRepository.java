package com.bookstudio.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstudio.author.dto.AuthorFlatDto;
import com.bookstudio.book.model.Book;
import com.bookstudio.book.relation.BookAuthor;
import com.bookstudio.book.relation.BookAuthorId;

public interface BookAuthorRepository extends JpaRepository<BookAuthor, BookAuthorId> {
    @Query("""
        SELECT new com.bookstudio.author.dto.AuthorFlatDto(
            a.authorId, a.name, a.biography, a.birthDate,
            a.photoUrl, a.status, a.nationality.nationalityId, a.nationality.name
        )
        FROM BookAuthor ba
        JOIN ba.author a
        WHERE ba.book.bookId = :bookId
    """)
    List<AuthorFlatDto> findAuthorFlatDtosByBookId(@Param("bookId") Long bookId);

    void deleteAllByBook(Book book);
}
