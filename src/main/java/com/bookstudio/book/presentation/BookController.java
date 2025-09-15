package com.bookstudio.book.presentation;

import com.bookstudio.book.application.BookService;
import com.bookstudio.book.domain.dto.request.CreateBookRequest;
import com.bookstudio.book.domain.dto.request.UpdateBookRequest;
import com.bookstudio.book.domain.dto.response.BookDetailResponse;
import com.bookstudio.book.domain.dto.response.BookListResponse;
import com.bookstudio.shared.domain.dto.response.ApiErrorResponse;
import com.bookstudio.shared.domain.dto.response.ApiResponse;
import com.bookstudio.shared.domain.dto.response.OptionResponse;
import com.bookstudio.shared.util.SelectOptions;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<?> list() {
        List<BookListResponse> books = bookService.getList();
        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiErrorResponse(false, "No books found.", "no_content", 204));
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            BookDetailResponse book = bookService.getDetailById(id);
            return ResponseEntity.ok(book);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, "Book not found.", "not_found", 404));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateBookRequest request) {
        try {
            BookListResponse created = bookService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponse(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateBookRequest request) {
        try {
            BookListResponse updated = bookService.update(id, request);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, e.getMessage(), "update_failed", 404));
        }
    }

    @GetMapping("/filter-options")
    public ResponseEntity<?> filterOptions() {
        try {
            List<OptionResponse> books = bookService.getOptions();

            if (books.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiErrorResponse(false, "No books found for filter.", "no_content", 204));
            }

            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(false, "Error fetching book filter options.", "server_error", 500));
        }
    }

    @GetMapping("/select-options")
    public ResponseEntity<?> selectOptions() {
        try {
            SelectOptions options = bookService.getSelectOptions();

            if ((options.getLanguages() != null && !options.getLanguages().isEmpty()) ||
                    (options.getPublishers() != null && !options.getPublishers().isEmpty()) ||
                    (options.getCategories() != null && !options.getCategories().isEmpty())) {

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
