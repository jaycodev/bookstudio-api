package com.bookstudio.copy.controller;

import com.bookstudio.copy.dto.CopyDetailDto;
import com.bookstudio.copy.dto.CopyListDto;
import com.bookstudio.copy.dto.CreateCopyDto;
import com.bookstudio.copy.dto.UpdateCopyDto;
import com.bookstudio.copy.service.CopyService;
import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;
import com.bookstudio.shared.util.SelectOptions;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/copies")
@RequiredArgsConstructor
public class CopyController {

    private final CopyService copyService;

    @GetMapping
    public ResponseEntity<?> list() {
        List<CopyListDto> copies = copyService.getList();
        if (copies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiError(false, "No copies found.", "no_content", 204));
        }
        return ResponseEntity.ok(copies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            CopyDetailDto copy = copyService.getInfoById(id);
            return ResponseEntity.ok(copy);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, "Copy not found.", "not_found", 404));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateCopyDto dto) {
        try {
            CopyListDto created = copyService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiError(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateCopyDto dto) {
        try {
            CopyListDto updated = copyService.update(dto);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, e.getMessage(), "update_failed", 404));
        }
    }

    @GetMapping("/select-options")
    public ResponseEntity<?> selectOptions() {
        try {
            SelectOptions options = copyService.getSelectOptions();

            if ((options.getBooks() != null && !options.getBooks().isEmpty()) ||
                    (options.getShelves() != null && !options.getShelves().isEmpty())) {

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
