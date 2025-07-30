package com.bookstudio.fine.controller;

import com.bookstudio.fine.dto.FineResponseDto;
import com.bookstudio.fine.dto.CreateFineDto;
import com.bookstudio.fine.dto.UpdateFineDto;
import com.bookstudio.fine.projection.FineInfoProjection;
import com.bookstudio.fine.projection.FineListProjection;
import com.bookstudio.fine.service.FineService;
import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fines")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;

    @GetMapping
    public ResponseEntity<?> list() {
        List<FineListProjection> fines = fineService.getList();
        if (fines.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiError(false, "No fines found.", "no_content", 204));
        }
        return ResponseEntity.ok(fines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        FineInfoProjection fine = fineService.getInfoById(id).orElse(null);
        if (fine == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, "Fine not found.", "not_found", 404));
        }
        return ResponseEntity.ok(fine);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateFineDto dto) {
        try {
            FineResponseDto created = fineService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiError(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateFineDto dto) {
        try {
            FineResponseDto updated = fineService.update(dto);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, e.getMessage(), "update_failed", 404));
        }
    }
}
