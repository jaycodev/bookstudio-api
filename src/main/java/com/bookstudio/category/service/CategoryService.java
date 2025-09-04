package com.bookstudio.category.service;

import com.bookstudio.category.dto.CategoryDetailDto;
import com.bookstudio.category.dto.CategoryListDto;
import com.bookstudio.category.dto.CategoryOptionDto;
import com.bookstudio.category.dto.CategorySummaryDto;
import com.bookstudio.category.dto.CreateCategoryDto;
import com.bookstudio.category.dto.UpdateCategoryDto;
import com.bookstudio.category.model.Category;
import com.bookstudio.category.repository.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryListDto> getList() {
        return categoryRepository.findList();
    }

    public Optional<Category> findById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public CategoryDetailDto getInfoById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + categoryId));

        return new CategoryDetailDto(
                category.getCategoryId(),
                category.getName(),
                category.getLevel().name(),
                category.getDescription(),
                category.getStatus().name());
    }

    @Transactional
    public CategoryListDto create(CreateCategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setLevel(dto.getLevel());
        category.setDescription(dto.getDescription());
        category.setStatus(dto.getStatus());

        Category saved = categoryRepository.save(category);
        return toListDto(saved);
    }

    @Transactional
    public CategoryListDto update(Long categoryId, UpdateCategoryDto dto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + categoryId));

        category.setName(dto.getName());
        category.setLevel(dto.getLevel());
        category.setDescription(dto.getDescription());
        category.setStatus(dto.getStatus());

        Category updated = categoryRepository.save(category);
        return toListDto(updated);
    }

    public List<CategoryOptionDto> getOptions() {
        return categoryRepository.findForOptions();
    }

    public CategorySummaryDto toSummaryDto(Category category) {
        return CategorySummaryDto.builder()
                .id(category.getCategoryId())
                .name(category.getName())
                .build();
    }

    private CategoryListDto toListDto(Category category) {
        return new CategoryListDto(
                category.getCategoryId(),
                category.getName(),
                category.getLevel(),
                category.getDescription(),
                category.getStatus());
    }
}
