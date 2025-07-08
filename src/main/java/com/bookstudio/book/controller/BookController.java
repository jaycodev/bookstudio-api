package com.bookstudio.book.controller;

import com.bookstudio.book.model.Book;
import com.bookstudio.book.service.BookService;
import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;
import com.bookstudio.shared.util.SelectOptions;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<?> listBooks() {
        List<Book> books = bookService.listBooks();
        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiError(false, "No books found.", "no_content", 204));
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {
        Book book = bookService.getBook(id).orElse(null);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, "Book not found.", "not_found", 404));
        }
        return ResponseEntity.ok(book);
    }

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        try {
			Book created = bookService.createBook(book);
			return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ApiError(false, e.getMessage(), "creation_failed", 400));
		}
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        try {
            Book result = bookService.updateBook(id, updatedBook);
            return ResponseEntity.ok(new ApiResponse(true, result));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, e.getMessage(), "update_failed", 404));
        }
    }

    @GetMapping("/select-options")
    public ResponseEntity<?> getSelectOptions() {
        try {
            SelectOptions options = bookService.populateSelects();

            if ((options.getAuthors() != null && !options.getAuthors().isEmpty()) ||
                    (options.getPublishers() != null && !options.getPublishers().isEmpty()) ||
                    (options.getCourses() != null && !options.getCourses().isEmpty()) ||
                    (options.getGenres() != null && !options.getGenres().isEmpty())) {

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
