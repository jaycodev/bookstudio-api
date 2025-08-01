package com.bookstudio.reader.controller;

import com.bookstudio.reader.dto.CreateReaderDto;
import com.bookstudio.reader.dto.ReaderDetailDto;
import com.bookstudio.reader.dto.ReaderListDto;
import com.bookstudio.reader.dto.UpdateReaderDto;
import com.bookstudio.reader.service.ReaderService;
import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;

import jakarta.persistence.EntityNotFoundException;
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
        List<ReaderListDto> readers = readerService.getList();
        if (readers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiError(false, "No readers found.", "no_content", 204));
        }
        return ResponseEntity.ok(readers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            ReaderDetailDto reader = readerService.getInfoById(id);
            return ResponseEntity.ok(reader);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, "Reader not found.", "not_found", 404));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateReaderDto dto) {
        try {
            ReaderListDto created = readerService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiError(false, e.getMessage(), "validation_error", 400));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(false, "Server error while creating reader.", "server_error", 500));
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateReaderDto dto) {
        try {
            ReaderListDto updated = readerService.update(dto);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, e.getMessage(), "update_failed", 404));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiError(false, e.getMessage(), "validation_error", 400));
        }
    }
}
