package com.bookstudio.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstudio.category.dto.CategoryListDto;
import com.bookstudio.category.dto.CategorySelectDto;
import com.bookstudio.category.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("""
        SELECT 
            new com.bookstudio.category.dto.CategoryListDto(
                c.categoryId,
                c.name,
                c.level,
                c.description,
                c.status
                )
        FROM Category c
        ORDER BY c.categoryId DESC
    """)
    List<CategoryListDto> findList();

    @Query("""
        SELECT 
            new com.bookstudio.category.dto.CategorySelectDto(
                c.categoryId,
                c.name
            )
        FROM Category c
        WHERE c.status = com.bookstudio.shared.enums.Status.activo
        ORDER BY c.name ASC
    """)
    List<CategorySelectDto> findForSelect();
}
