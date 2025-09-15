package com.bookstudio.author.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstudio.author.domain.dto.response.AuthorDetailResponse;
import com.bookstudio.author.domain.dto.response.AuthorListResponse;
import com.bookstudio.author.domain.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("""
        SELECT new com.bookstudio.author.domain.dto.response.AuthorListResponse(
            a.id,
            a.photoUrl,
            a.name,
            new com.bookstudio.author.domain.dto.response.AuthorListResponse$Nationality(
                n.id,
                n.code,
                n.name
            ),
            a.birthDate,
            a.status
        )
        FROM Author a
        JOIN a.nationality n
        ORDER BY a.id DESC
    """)
    List<AuthorListResponse> findList();

    @Query("""
        SELECT new com.bookstudio.author.domain.dto.response.AuthorDetailResponse(
            a.id,
            a.name,
            new com.bookstudio.author.domain.dto.response.AuthorDetailResponse$Nationality(
                n.id,
                n.code,
                n.name
            ),
            a.birthDate,
            a.biography,
            a.status,
            a.photoUrl
        )
        FROM Author a
        JOIN a.nationality n
        WHERE a.id = :id
    """)
    Optional<AuthorDetailResponse> findDetailById(Long id);
}
