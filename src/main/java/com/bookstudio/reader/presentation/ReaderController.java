package com.bookstudio.reader.presentation;

import com.bookstudio.reader.application.ReaderService;
import com.bookstudio.reader.application.dto.request.CreateReaderRequest;
import com.bookstudio.reader.application.dto.request.UpdateReaderRequest;
import com.bookstudio.reader.application.dto.response.ReaderDetailResponse;
import com.bookstudio.reader.application.dto.response.ReaderListResponse;
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
@RequestMapping("/readers")
@RequiredArgsConstructor
@Tag(name = "Readers", description = "Operations related to readers")
public class ReaderController {
    private final ReaderService readerService;

    @GetMapping
    @Operation(summary = "List all readers")
    public ResponseEntity<?> list() {
        List<ReaderListResponse> readers = readerService.getList();
        if (readers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiErrorResponse(false, "No readers found.", "no_content", 204));
        }
        return ResponseEntity.ok(readers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a reader by ID")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            ReaderDetailResponse reader = readerService.getDetailById(id);
            return ResponseEntity.ok(reader);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, "Reader not found.", "not_found", 404));
        }
    }

    @PostMapping
    @Operation(summary = "Create a new reader")
    public ResponseEntity<?> create(@RequestBody CreateReaderRequest request) {
        try {
            ReaderListResponse created = readerService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponse(false, e.getMessage(), "validation_error", 400));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(false, "Server error while creating reader.", "server_error", 500));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a reader by ID")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateReaderRequest request) {
        try {
            ReaderListResponse updated = readerService.update(id, request);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, e.getMessage(), "update_failed", 404));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponse(false, e.getMessage(), "validation_error", 400));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(false, "Server error while updating reader.", "server_error", 500));
        }
    }

    @GetMapping("/filter-options")
    @Operation(summary = "Get reader filter options")
    public ResponseEntity<?> filterOptions() {
        try {
            List<OptionResponse> readers = readerService.getOptions();
            if (readers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiErrorResponse(false, "No readers found for filter.", "no_content", 204));
            }
            return ResponseEntity.ok(readers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(false, "Error fetching reader filter options.", "server_error", 500));
        }
    }
}
