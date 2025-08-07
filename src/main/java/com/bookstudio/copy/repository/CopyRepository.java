package com.bookstudio.copy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstudio.book.model.Book;
import com.bookstudio.copy.dto.CopyListDto;
import com.bookstudio.copy.dto.CopySelectDto;
import com.bookstudio.copy.model.Copy;

public interface CopyRepository extends JpaRepository<Copy, Long> {
    @Query("""
        SELECT new com.bookstudio.copy.dto.CopyListDto(
            c.code,
            b.coverUrl,
            b.title,
            s.code,
            s.floor,
            l.name,
            c.isAvailable,
            c.condition,
            c.copyId
        )
        FROM Copy c
        JOIN c.book b
        JOIN c.shelf s
        JOIN s.location l
        ORDER BY c.copyId DESC
    """)
    List<CopyListDto> findList();

    @Query("""
        SELECT new com.bookstudio.copy.dto.CopySelectDto(
            c.copyId,
            c.code
        )
        FROM Copy c
        WHERE c.isAvailable = true
    """)
    List<CopySelectDto> findForSelect();

    Long countByBookAndIsAvailableFalse(Book book);
    Long countByBookAndIsAvailableTrue(Book book);
}
