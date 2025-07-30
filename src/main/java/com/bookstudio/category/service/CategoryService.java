package com.bookstudio.category.service;

import com.bookstudio.category.dto.CategoryResponseDto;
import com.bookstudio.category.dto.CreateCategoryDto;
import com.bookstudio.category.dto.UpdateCategoryDto;
import com.bookstudio.category.model.Category;
import com.bookstudio.category.projection.CategorySelectProjection;
import com.bookstudio.category.projection.CategoryViewProjection;
import com.bookstudio.category.repository.CategoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryViewProjection> getList() {
        return categoryRepository.findList();
    }

    public Optional<Category> findById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public Optional<CategoryViewProjection> getInfoById(Long categoryId) {
        return categoryRepository.findInfoById(categoryId);
    }

    @Transactional
    public CategoryResponseDto create(CreateCategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setLevel(dto.getLevel());
        category.setDescription(dto.getDescription());
        category.setStatus(dto.getStatus());

        Category saved = categoryRepository.save(category);

        return new CategoryResponseDto(
                saved.getCategoryId(),
                saved.getName(),
                saved.getLevel().name(),
                saved.getDescription(),
                saved.getStatus().name());
    }

    @Transactional
    public CategoryResponseDto update(UpdateCategoryDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + dto.getCategoryId()));

        category.setName(dto.getName());
        category.setLevel(dto.getLevel());
        category.setDescription(dto.getDescription());
        category.setStatus(dto.getStatus());

        Category updated = categoryRepository.save(category);

        return new CategoryResponseDto(
                updated.getCategoryId(),
                updated.getName(),
                updated.getLevel().name(),
                updated.getDescription(),
                updated.getStatus().name());
    }

    public List<CategorySelectProjection> getForSelect() {
        return categoryRepository.findForSelect();
    }
}
