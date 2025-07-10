package com.bookstudio.student.controller;

import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;
import com.bookstudio.shared.util.SelectOptions;
import com.bookstudio.shared.util.ValidationErrorResponse;
import com.bookstudio.shared.util.FieldErrorDetail;
import com.bookstudio.student.dto.CreateStudentDto;
import com.bookstudio.student.dto.StudentResponseDto;
import com.bookstudio.student.dto.UpdateStudentDto;
import com.bookstudio.student.projection.StudentInfoProjection;
import com.bookstudio.student.projection.StudentListProjection;
import com.bookstudio.student.service.StudentService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<?> list() {
        List<StudentListProjection> students = studentService.getList();
        if (students.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiError(false, "No students found.", "no_content", 204));
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        StudentInfoProjection student = studentService.getInfoById(id).orElse(null);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, "Student not found.", "not_found", 404));
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateStudentDto dto) {
        try {
            StudentResponseDto created = studentService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (RuntimeException e) {
            return handleFieldError(e.getMessage(), "addStudentEmail", "addStudentDNI");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(false, "Server error while creating student.", "server_error", 500));
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateStudentDto dto) {
        try {
            StudentResponseDto updated = studentService.update(dto);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (RuntimeException e) {
            return handleFieldError(e.getMessage(), "editStudentEmail", null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(false, "Server error while updating student.", "server_error", 500));
        }
    }

    @GetMapping("/options")
    public ResponseEntity<?> selectOptions() {
        try {
            SelectOptions options = studentService.getSelectOptions();
            if (options.getFaculties() != null && !options.getFaculties().isEmpty()) {
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
