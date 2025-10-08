package com.bookstudio.worker.presentation;

import com.bookstudio.shared.application.dto.response.ApiErrorResponse;
import com.bookstudio.shared.application.dto.response.ApiResponse;
import com.bookstudio.shared.util.SelectOptions;
import com.bookstudio.worker.application.WorkerService;
import com.bookstudio.worker.application.dto.request.CreateWorkerRequest;
import com.bookstudio.worker.application.dto.request.UpdateWorkerRequest;
import com.bookstudio.worker.application.dto.response.WorkerDetailResponse;
import com.bookstudio.worker.application.dto.response.WorkerListResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
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

import static com.bookstudio.auth.util.LoginConstants.ID;

@RestController
@RequestMapping("/workers")
@RequiredArgsConstructor
@Tag(name = "Workers", description = "Operations related to workers")
public class WorkerController {
    private final WorkerService workerService;

    @GetMapping
    @Operation(summary = "List all workers")
    public ResponseEntity<?> list(HttpSession session) {
        Object loggedIdObj = session.getAttribute(ID);
        if (loggedIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiErrorResponse(false, "No user session.", "unauthorized", 401));
        }

        Long loggedId = Long.valueOf(loggedIdObj.toString());
        List<WorkerListResponse> workers = workerService.getList(loggedId);

        if (workers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiErrorResponse(false, "No workers found.", "no_content", 204));
        }

        return ResponseEntity.ok(workers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a worker by ID")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            WorkerDetailResponse worker = workerService.getDetailById(id);
            return ResponseEntity.ok(worker);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, "Worker not found.", "not_found", 404));
        }
    }

    @PostMapping
    @Operation(summary = "Create a new worker")
    public ResponseEntity<?> create(@RequestBody CreateWorkerRequest request) {
        try {
            WorkerListResponse created = workerService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiErrorResponse(false, e.getMessage(), "validation_error", 400));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(false, "Server error while creating worker.", "server_error", 500));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a worker by ID")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateWorkerRequest request) {
        try {
            WorkerListResponse updated = workerService.update(id, request);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, e.getMessage(), "update_failed", 404));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponse(false, e.getMessage(), "validation_error", 400));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(false, "Server error while updating worker.", "server_error", 500));
        }
    }

    @GetMapping("/select-options")
    @Operation(summary = "Get select options for workers")
    public ResponseEntity<?> selectOptions() {
        SelectOptions options = workerService.getSelectOptions();

        boolean hasOptions = !options.roles().isEmpty();

        if (hasOptions) {
            return ResponseEntity.ok(options);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiErrorResponse(false, "No select options found.", "no_content", 204));
        }
    }
}
