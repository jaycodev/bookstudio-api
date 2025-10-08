package com.bookstudio.copy.presentation;

import com.bookstudio.copy.application.CopyService;
import com.bookstudio.copy.application.dto.request.CreateCopyRequest;
import com.bookstudio.copy.application.dto.request.UpdateCopyRequest;
import com.bookstudio.copy.application.dto.response.CopyDetailResponse;
import com.bookstudio.copy.application.dto.response.CopyListResponse;
import com.bookstudio.shared.application.dto.response.ApiErrorResponse;
import com.bookstudio.shared.application.dto.response.ApiResponse;
import com.bookstudio.shared.application.dto.response.OptionResponse;
import com.bookstudio.shared.util.SelectOptions;

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
@RequestMapping("/copies")
@RequiredArgsConstructor
@Tag(name = "Copies", description = "Operations related to copies")
public class CopyController {
    private final CopyService copyService;

    @GetMapping
    @Operation(summary = "List all copies")
    public ResponseEntity<?> list() {
        List<CopyListResponse> copies = copyService.getList();
        if (copies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiErrorResponse(false, "No copies found.", "no_content", 204));
        }
        return ResponseEntity.ok(copies);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a copy by ID")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            CopyDetailResponse copy = copyService.getDetailById(id);
            return ResponseEntity.ok(copy);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, "Copy not found.", "not_found", 404));
        }
    }

    @PostMapping
    @Operation(summary = "Create a new copy")
    public ResponseEntity<?> create(@RequestBody CreateCopyRequest request) {
        try {
            CopyListResponse created = copyService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponse(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a copy by ID")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateCopyRequest request) {
        try {
            CopyListResponse updated = copyService.update(id, request);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, e.getMessage(), "update_failed", 404));
        }
    }

    @GetMapping("/filter-options")
    @Operation(summary = "Get copy filter options")
    public ResponseEntity<?> filterOptions() {
        try {
            List<OptionResponse> copies = copyService.getOptions();
            if (copies.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiErrorResponse(false, "No copies found for filter.", "no_content", 204));
            }
            return ResponseEntity.ok(copies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(false, "Error fetching copy filter options.", "server_error", 500));
        }
    }

    @GetMapping("/select-options")
    @Operation(summary = "Get select options for copies")
    public ResponseEntity<?> selectOptions() {
        SelectOptions options = copyService.getSelectOptions();
        boolean hasOptions = !options.books().isEmpty() || !options.shelves().isEmpty();

        if (hasOptions) {
            return ResponseEntity.ok(options);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiErrorResponse(false, "No select options found.", "no_content", 204));
        }
    }
}
