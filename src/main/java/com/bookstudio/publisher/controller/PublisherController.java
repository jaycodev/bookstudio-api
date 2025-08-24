package com.bookstudio.publisher.controller;

import com.bookstudio.publisher.dto.CreatePublisherDto;
import com.bookstudio.publisher.dto.PublisherDetailDto;
import com.bookstudio.publisher.dto.PublisherListDto;
import com.bookstudio.publisher.dto.UpdatePublisherDto;
import com.bookstudio.publisher.service.PublisherService;
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
@RequestMapping("/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping
    public ResponseEntity<?> list() {
        List<PublisherListDto> publishers = publisherService.getList();
        if (publishers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiError(false, "No publishers found.", "no_content", 204));
        }
        return ResponseEntity.ok(publishers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            PublisherDetailDto publisher = publisherService.getInfoById(id);
            return ResponseEntity.ok(publisher);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, "Publisher not found.", "not_found", 404));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreatePublisherDto dto) {
        try {
            PublisherListDto created = publisherService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiError(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdatePublisherDto dto) {
        try {
            PublisherListDto updated = publisherService.update(id, dto);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, e.getMessage(), "update_failed", 404));
        }
    }

    @GetMapping("/select-options")
    public ResponseEntity<?> selectOptions() {
        try {
            SelectOptions options = publisherService.getSelectOptions();
            if ((options.getNationalities() != null && !options.getNationalities().isEmpty())) {
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
