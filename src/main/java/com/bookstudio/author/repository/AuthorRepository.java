package com.bookstudio.author.repository;

import com.bookstudio.author.model.Author;
import com.bookstudio.author.projection.AuthorSelectProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("SELECT a.id as authorId, a.name as name FROM Author a WHERE a.status = 'activo'")
    List<AuthorSelectProjection> findForSelect();
}
