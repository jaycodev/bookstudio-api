package com.bookstudio.category.controller;

import com.bookstudio.category.dto.CategoryDetailDto;
import com.bookstudio.category.dto.CategoryListDto;
import com.bookstudio.category.dto.CategoryOptionDto;
import com.bookstudio.category.dto.CreateCategoryDto;
import com.bookstudio.category.dto.UpdateCategoryDto;
import com.bookstudio.category.service.CategoryService;
import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> list() {
        List<CategoryListDto> categories = categoryService.getList();
        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiError(false, "No categories found.", "no_content", 204));
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            CategoryDetailDto category = categoryService.getInfoById(id);
            return ResponseEntity.ok(category);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, "Category not found.", "not_found", 404));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateCategoryDto dto) {
        try {
            CategoryListDto created = categoryService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiError(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateCategoryDto dto) {
        try {
            CategoryListDto updated = categoryService.update(id, dto);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, e.getMessage(), "update_failed", 404));
        }
    }

    @GetMapping("/filter-options")
    public ResponseEntity<?> filterOptions() {
        try {
            List<CategoryOptionDto> categories = categoryService.getOptions();

            if (categories.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiError(false, "No categories found for filter.", "no_content", 204));
            }

            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(false, "Error fetching category filter options.", "server_error", 500));
        }
    }
}
