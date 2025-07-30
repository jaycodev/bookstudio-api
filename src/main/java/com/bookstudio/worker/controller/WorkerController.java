package com.bookstudio.worker.controller;

import com.bookstudio.auth.util.LoginConstants;
import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;
import com.bookstudio.shared.util.ValidationErrorResponse;
import com.bookstudio.worker.dto.CreateWorkerDto;
import com.bookstudio.worker.dto.UpdateWorkerDto;
import com.bookstudio.worker.dto.WorkerResponseDto;
import com.bookstudio.worker.projection.WorkerViewProjection;
import com.bookstudio.worker.service.WorkerService;
import com.bookstudio.shared.util.FieldErrorDetail;
import com.bookstudio.shared.util.SelectOptions;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workers")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @GetMapping
    public ResponseEntity<?> list(HttpSession session) {
        Object loggedIdObj = session.getAttribute(LoginConstants.ID);
        if (loggedIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiError(false, "No user session.", "unauthorized", 401));
        }

        Long loggedId = Long.valueOf(loggedIdObj.toString());
        List<WorkerViewProjection> users = workerService.getList(loggedId);

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiError(false, "No workers found.", "no_content", 204));
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        WorkerViewProjection user = workerService.getInfoById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, "Worker not found.", "not_found", 404));
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateWorkerDto dto) {
        try {
            WorkerResponseDto created = workerService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (RuntimeException e) {
            return handleFieldError(e.getMessage(), "addEmail", "addUsername");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(false, "Server error while creating worker.", "server_error", 500));
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateWorkerDto dto) {
        try {
            WorkerResponseDto updated = workerService.update(dto);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (RuntimeException e) {
            return handleFieldError(e.getMessage(), "editEmail", "editUsername");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(false, "Server error while updating worker.", "server_error", 500));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean deleted = workerService.delete(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponse(true));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, "Worker not found.", "deletion_failed", 404));
        }
    }

    private ResponseEntity<?> handleFieldError(String message, String emailField, String usernameField) {
        String lower = message.toLowerCase();
        if (lower.contains("correo electrónico") && emailField != null) {
            return ResponseEntity.badRequest().body(new ValidationErrorResponse(
                    false, "validation_error", 400,
                    List.of(new FieldErrorDetail(emailField, message))));
        } else if (lower.contains("usuario") && usernameField != null) {
            return ResponseEntity.badRequest().body(new ValidationErrorResponse(
                    false, "validation_error", 400,
                    List.of(new FieldErrorDetail(usernameField, message))));
        } else {
            return ResponseEntity.badRequest().body(new ApiError(false, message, "validation_error", 400));
        }
    }

    @GetMapping("/select-options")
    public ResponseEntity<?> selectOptions() {
        try {
            SelectOptions options = workerService.getSelectOptions();
            if ((options.getRoles() != null && !options.getRoles().isEmpty())) {
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
