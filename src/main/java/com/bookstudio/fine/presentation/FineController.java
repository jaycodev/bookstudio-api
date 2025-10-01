package com.bookstudio.fine.presentation;

import com.bookstudio.fine.application.FineService;
import com.bookstudio.fine.application.dto.request.CreateFineRequest;
import com.bookstudio.fine.application.dto.request.UpdateFineRequest;
import com.bookstudio.fine.application.dto.response.FineDetailResponse;
import com.bookstudio.fine.application.dto.response.FineListResponse;
import com.bookstudio.shared.application.dto.response.ApiErrorResponse;
import com.bookstudio.shared.application.dto.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fines")
@RequiredArgsConstructor
@Tag(name = "Fines", description = "Operations related to fines")
public class FineController {

    private final FineService fineService;

    @GetMapping
    @Operation(summary = "List all fines")
    public ResponseEntity<?> list() {
        List<FineListResponse> fines = fineService.getList();
        if (fines.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiErrorResponse(false, "No fines found.", "no_content", 204));
        }
        return ResponseEntity.ok(fines);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a fine by ID")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            FineDetailResponse fine = fineService.getDetailById(id);
            return ResponseEntity.ok(fine);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, "Fine not found.", "not_found", 404));
        }
    }

    @PostMapping
    @Operation(summary = "Create a new fine")
    public ResponseEntity<?> create(@RequestBody CreateFineRequest request) {
        try {
            FineListResponse created = fineService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponse(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a fine by ID")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateFineRequest request) {
        try {
            FineListResponse updated = fineService.update(id, request);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, e.getMessage(), "update_failed", 404));
        }
    }
}
