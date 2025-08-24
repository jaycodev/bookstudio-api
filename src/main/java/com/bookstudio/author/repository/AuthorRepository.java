package com.bookstudio.author.repository;

import com.bookstudio.author.dto.AuthorListDto;
import com.bookstudio.author.dto.AuthorSelectDto;
import com.bookstudio.author.model.Author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("""
        SELECT 
            new com.bookstudio.author.dto.AuthorListDto(
                a.authorId,
                a.photoUrl,
                a.name,
                n.name,
                a.birthDate,
                a.status
            )
        FROM Author a
        JOIN a.nationality n
        ORDER BY a.authorId DESC
    """)
    List<AuthorListDto> findList();

    @Query("""
        SELECT 
            new com.bookstudio.author.dto.AuthorSelectDto(
                a.authorId,
                a.name
            )
        FROM Author a
        WHERE a.status = com.bookstudio.shared.enums.Status.activo
        ORDER BY a.name ASC
    """)
    List<AuthorSelectDto> findForSelect();
}
