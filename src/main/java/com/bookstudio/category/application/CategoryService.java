package com.bookstudio.category.application;

import com.bookstudio.category.infrastructure.repository.CategoryRepository;
import com.bookstudio.shared.exception.ResourceNotFoundException;
import com.bookstudio.shared.response.OptionResponse;
import com.bookstudio.category.application.dto.request.CreateCategoryRequest;
import com.bookstudio.category.application.dto.request.UpdateCategoryRequest;
import com.bookstudio.category.application.dto.response.CategoryDetailResponse;
import com.bookstudio.category.application.dto.response.CategoryListResponse;
import com.bookstudio.category.domain.model.Category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryListResponse> getList() {
        return categoryRepository.findList();
    }

    public List<OptionResponse> getOptions() {
        return categoryRepository.findForOptions();
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public CategoryDetailResponse getDetailById(Long id) {
        return categoryRepository.findDetailById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
    }

    @Transactional
    public CategoryListResponse create(CreateCategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        category.setLevel(request.level());
        category.setDescription(request.description());
        category.setStatus(request.status());

        Category saved = categoryRepository.save(category);

        return toListResponse(saved);
    }

    @Transactional
    public CategoryListResponse update(Long id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        category.setName(request.name());
        category.setLevel(request.level());
        category.setDescription(request.description());
        category.setStatus(request.status());

        Category updated = categoryRepository.save(category);

        return toListResponse(updated);
    }

    private CategoryListResponse toListResponse(Category category) {
        return new CategoryListResponse(
                category.getId(),
                category.getName(),
                category.getLevel(),
                category.getDescription(),
                category.getStatus());
    }
}
