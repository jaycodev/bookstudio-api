package com.bookstudio.category.presentation;

import com.bookstudio.category.application.CategoryService;
import com.bookstudio.category.application.dto.request.CreateCategoryRequest;
import com.bookstudio.category.application.dto.request.UpdateCategoryRequest;
import com.bookstudio.category.application.dto.response.CategoryDetailResponse;
import com.bookstudio.category.application.dto.response.CategoryListResponse;
import com.bookstudio.shared.application.dto.response.ApiErrorResponse;
import com.bookstudio.shared.application.dto.response.ApiResponse;
import com.bookstudio.shared.application.dto.response.OptionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Operations related to categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "List all categories")
    public ResponseEntity<?> list() {
        List<CategoryListResponse> categories = categoryService.getList();
        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiErrorResponse(false, "No categories found.", "no_content", 204));
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a category by ID")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            CategoryDetailResponse category = categoryService.getDetailById(id);
            return ResponseEntity.ok(category);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, "Category not found.", "not_found", 404));
        }
    }

    @PostMapping
    @Operation(summary = "Create a new category")
    public ResponseEntity<?> create(@RequestBody CreateCategoryRequest request) {
        try {
            CategoryListResponse created = categoryService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponse(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category by ID")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateCategoryRequest request) {
        try {
            CategoryListResponse updated = categoryService.update(id, request);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, e.getMessage(), "update_failed", 404));
        }
    }

    @GetMapping("/filter-options")
    @Operation(summary = "Get category filter options")
    public ResponseEntity<?> filterOptions() {
        try {
            List<OptionResponse> categories = categoryService.getOptions();
            if (categories.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiErrorResponse(false, "No categories found for filter.", "no_content", 204));
            }
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(false, "Error fetching category filter options.", "server_error", 500));
        }
    }
}
