package com.bookstudio.category.service;

import com.bookstudio.category.dto.CategoryDto;
import com.bookstudio.category.dto.CategoryListDto;
import com.bookstudio.category.dto.CategorySelectDto;
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

    public CategoryDto getInfoById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + categoryId));

        return toDto(category);
    }

    @Transactional
    public CategoryDto create(CreateCategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setLevel(dto.getLevel());
        category.setDescription(dto.getDescription());
        category.setStatus(dto.getStatus());

        Category saved = categoryRepository.save(category);
        return toDto(saved);
    }

    @Transactional
    public CategoryDto update(UpdateCategoryDto dto) {
        Category category = categoryRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + dto.getId()));

        category.setName(dto.getName());
        category.setLevel(dto.getLevel());
        category.setDescription(dto.getDescription());
        category.setStatus(dto.getStatus());

        Category updated = categoryRepository.save(category);
        return toDto(updated);
    }

    public List<CategorySelectDto> getForSelect() {
        return categoryRepository.findForSelect();
    }

    private CategoryDto toDto(Category category) {
        return new CategoryDto(
                category.getCategoryId(),
                category.getName(),
                category.getLevel().name(),
                category.getDescription(),
                category.getStatus().name());
    }
}
