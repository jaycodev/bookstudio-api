package com.bookstudio.author.controller;

import com.bookstudio.author.model.Author;
import com.bookstudio.author.service.AuthorService;
import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;
import com.bookstudio.shared.util.SelectOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<?> listAuthors() {
        List<Author> authors = authorService.listAuthors();
        if (authors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiError(false, "No authors found.", "no_content", 204));
        }
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthor(@PathVariable Long id) {
        Author author = authorService.getAuthor(id).orElse(null);
        if (author == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, "Author not found.", "not_found", 404));
        }
        return ResponseEntity.ok(author);
    }

    @PostMapping
    public ResponseEntity<?> createAuthor(@ModelAttribute Author author) {
        try {
            Author created = authorService.createAuthor(author);
            if (created.getPhoto() != null) {
                created.setPhotoBase64("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(created.getPhoto()));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiError(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthor(@PathVariable Long id, @ModelAttribute Author updatedData) {
        try {
            Author updated = authorService.updateAuthor(id, updatedData);
            if (updated.getPhoto() != null) {
                updated.setPhotoBase64("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(updated.getPhoto()));
                updated.setPhoto(null);
            }
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, e.getMessage(), "update_failed", 404));
        }
    }

    @GetMapping("/select-options")
    public ResponseEntity<?> getSelectOptions() {
        try {
            SelectOptions options = authorService.populateSelects();
            if ((options.getNationalities() != null && !options.getNationalities().isEmpty()) ||
                (options.getLiteraryGenres() != null && !options.getLiteraryGenres().isEmpty())) {
                return ResponseEntity.ok(options);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiError(false, "No select options found.", "no_content", 204));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(false, "Error populating select options.", "server_error", 500));
        }
    }
} 
