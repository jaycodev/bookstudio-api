package com.bookstudio.author.presentation;

import com.bookstudio.author.application.AuthorService;
import com.bookstudio.author.application.dto.request.CreateAuthorRequest;
import com.bookstudio.author.application.dto.request.UpdateAuthorRequest;
import com.bookstudio.author.application.dto.response.AuthorDetailResponse;
import com.bookstudio.author.application.dto.response.AuthorListResponse;
import com.bookstudio.shared.application.dto.response.ApiErrorResponse;
import com.bookstudio.shared.application.dto.response.ApiResponse;
import com.bookstudio.shared.util.SelectOptions;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
@Tag(name = "Authors", description = "Operations related to authors")
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    @Operation(summary = "List all authors")
    public ResponseEntity<?> list() {
        List<AuthorListResponse> authors = authorService.getList();
        if (authors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiErrorResponse(false, "No authors found.", "no_content", 204));
        }
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an author by ID")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            AuthorDetailResponse author = authorService.getDetailById(id);
            return ResponseEntity.ok(author);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, "Author not found.", "not_found", 404));
        }
    }

    @PostMapping
    @Operation(summary = "Create a new author")
    public ResponseEntity<?> create(@RequestBody CreateAuthorRequest request) {
        try {
            AuthorListResponse created = authorService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponse(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an author by ID")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateAuthorRequest request) {
        try {
            AuthorListResponse updated = authorService.update(id, request);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, e.getMessage(), "update_failed", 404));
        }
    }

    @GetMapping("/select-options")
    @Operation(summary = "Get select options for authors")
    public ResponseEntity<?> selectOptions() {
        try {
            SelectOptions options = authorService.getSelectOptions();
            if ((options.getNationalities() != null && !options.getNationalities().isEmpty())) {
                return ResponseEntity.ok(options);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiErrorResponse(false, "No select options found.", "no_content", 204));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(false, "Error populating select options.", "server_error", 500));
        }
    }
}
