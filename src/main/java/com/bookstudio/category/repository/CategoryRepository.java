package com.bookstudio.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstudio.category.model.Category;
import com.bookstudio.category.projection.CategorySelectProjection;
import com.bookstudio.category.projection.CategoryViewProjection;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("""
        SELECT 
            c.categoryId AS categoryId,
            c.name AS name,
            c.level AS level,
            c.description AS description,
            c.status AS status
        FROM Category c
        ORDER BY c.categoryId DESC
    """)
    List<CategoryViewProjection> findList();

    @Query("""
        SELECT 
            c.categoryId AS categoryId,
            c.name AS name,
            c.level AS level,
            c.description AS description,
            c.status AS status
        FROM Category c
        WHERE c.categoryId = :id
    """)
    Optional<CategoryViewProjection> findInfoById(@Param("id") Long id);

    @Query("""
        SELECT 
            c.categoryId AS categoryId,
            c.name AS name
        FROM Category c
        WHERE c.status = 'activo'
        ORDER BY c.name ASC
    """)
    List<CategorySelectProjection> findForSelect();
}
