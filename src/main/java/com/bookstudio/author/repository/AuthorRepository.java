package com.bookstudio.author.repository;

import com.bookstudio.author.model.Author;
import com.bookstudio.author.projection.AuthorDetailProjection;
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
            a.id AS authorId,
            a.name AS name,
            n.name AS nationalityName,
            lg.name AS literaryGenreName,
            a.birthDate AS birthDate,
            a.status AS status,
            a.photo AS photo
        FROM Author a
        JOIN a.nationality n
        JOIN a.literaryGenre lg
    """)
    List<AuthorListProjection> findList();

    @Query("""
        SELECT
            a.id AS authorId,
            a.name AS name,

            n.nationalityId AS nationalityId,
            n.name AS nationalityName,

            lg.literaryGenreId AS literaryGenreId,
            lg.name AS literaryGenreName,

            a.birthDate AS birthDate,
            a.biography AS biography,
            a.status AS status,
            a.photo AS photo
        FROM Author a
        JOIN a.nationality n
        JOIN a.literaryGenre lg
        WHERE a.id = :id
    """)
    Optional<AuthorDetailProjection> findDetailById(@Param("id") Long id);

    @Query("SELECT a.id AS authorId, a.name AS name FROM Author a WHERE a.status = 'activo'")
    List<AuthorSelectProjection> findForSelect();
}
