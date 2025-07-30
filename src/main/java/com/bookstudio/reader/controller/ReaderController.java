package com.bookstudio.reader.controller;

import com.bookstudio.reader.dto.CreateReaderDto;
import com.bookstudio.reader.dto.ReaderResponseDto;
import com.bookstudio.reader.dto.UpdateReaderDto;
import com.bookstudio.reader.projection.ReaderInfoProjection;
import com.bookstudio.reader.projection.ReaderListProjection;
import com.bookstudio.reader.service.ReaderService;
import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;
import com.bookstudio.shared.util.ValidationErrorResponse;
import com.bookstudio.shared.util.FieldErrorDetail;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/readers")
@RequiredArgsConstructor
public class ReaderController {

    private final ReaderService readerService;

    @GetMapping
    public ResponseEntity<?> list() {
        List<ReaderListProjection> students = readerService.getList();
        if (students.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiError(false, "No students found.", "no_content", 204));
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        ReaderInfoProjection student = readerService.getInfoById(id).orElse(null);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, "Student not found.", "not_found", 404));
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateReaderDto dto) {
        try {
            ReaderResponseDto created = readerService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (RuntimeException e) {
            return handleFieldError(e.getMessage(), "addEmail", "addDNI");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(false, "Server error while creating student.", "server_error", 500));
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateReaderDto dto) {
        try {
            ReaderResponseDto updated = readerService.update(dto);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (RuntimeException e) {
            return handleFieldError(e.getMessage(), "editEmail", null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(false, "Server error while updating student.", "server_error", 500));
        }
    }

    private ResponseEntity<?> handleFieldError(String message, String emailField, String dniField) {
        String lowerMessage = message.toLowerCase();
        if (lowerMessage.contains("correo electrónico") && emailField != null) {
            return ResponseEntity.badRequest().body(new ValidationErrorResponse(
                    false, "validation_error", 400,
                    List.of(new FieldErrorDetail(emailField, message))
            ));
        } else if (lowerMessage.contains("dni") && dniField != null) {
            return ResponseEntity.badRequest().body(new ValidationErrorResponse(
                    false, "validation_error", 400,
                    List.of(new FieldErrorDetail(dniField, message))
            ));
        } else {
            return ResponseEntity.badRequest().body(new ApiError(false, message, "validation_error", 400));
        }
    }
}
