package com.bookstudio.author.repository;

import com.bookstudio.author.model.Author;
import com.bookstudio.author.projection.AuthorInfoProjection;
import com.bookstudio.author.projection.AuthorListProjection;
import com.bookstudio.author.projection.AuthorSelectProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("""
        SELECT 
            a.authorId AS authorId,
            a.name AS name,
            n.name AS nationalityName,
            a.birthDate AS birthDate,
            a.status AS status,
            a.photoUrl AS photoUrl
        FROM Author a
        JOIN a.nationality n
        ORDER BY a.authorId DESC
    """)
    List<AuthorListProjection> findList();

    @Query("""
        SELECT
            a.authorId AS authorId,
            a.name AS name,
            n.nationalityId AS nationalityId,
            n.name AS nationalityName,
            a.birthDate AS birthDate,
            a.biography AS biography,
            a.status AS status,
            a.photoUrl AS photoUrl
        FROM Author a
        JOIN a.nationality n
        WHERE a.authorId = :id
    """)
    Optional<AuthorInfoProjection> findInfoById(@Param("id") Long id);

    @Query("SELECT a.authorId AS authorId, a.name AS name FROM Author a WHERE a.status = 'activo'")
    List<AuthorSelectProjection> findForSelect();
}
