package com.bookstudio.publisher.presentation;

import com.bookstudio.publisher.application.PublisherService;
import com.bookstudio.publisher.application.dto.request.CreatePublisherRequest;
import com.bookstudio.publisher.application.dto.request.UpdatePublisherRequest;
import com.bookstudio.publisher.application.dto.response.PublisherDetailResponse;
import com.bookstudio.publisher.application.dto.response.PublisherListResponse;
import com.bookstudio.shared.application.dto.response.ApiErrorResponse;
import com.bookstudio.shared.application.dto.response.ApiResponse;
import com.bookstudio.shared.application.dto.response.OptionResponse;
import com.bookstudio.shared.util.SelectOptions;

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
@RequestMapping("/publishers")
@RequiredArgsConstructor
@Tag(name = "Publishers", description = "Operations related to publishers")
public class PublisherController {
    private final PublisherService publisherService;

    @GetMapping
    @Operation(summary = "List all publishers")
    public ResponseEntity<?> list() {
        List<PublisherListResponse> publishers = publisherService.getList();
        if (publishers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiErrorResponse(false, "No publishers found.", "no_content", 204));
        }
        return ResponseEntity.ok(publishers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a publisher by ID")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            PublisherDetailResponse publisher = publisherService.getDetailById(id);
            return ResponseEntity.ok(publisher);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, "Publisher not found.", "not_found", 404));
        }
    }

    @PostMapping
    @Operation(summary = "Create a new publisher")
    public ResponseEntity<?> create(@RequestBody CreatePublisherRequest request) {
        try {
            PublisherListResponse created = publisherService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponse(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a publisher by ID")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdatePublisherRequest request) {
        try {
            PublisherListResponse updated = publisherService.update(id, request);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, e.getMessage(), "update_failed", 404));
        }
    }

    @GetMapping("/filter-options")
    @Operation(summary = "Get publisher filter options")
    public ResponseEntity<?> filterOptions() {
        try {
            List<OptionResponse> publishers = publisherService.getOptions();
            if (publishers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiErrorResponse(false, "No publishers found for filter.", "no_content", 204));
            }
            return ResponseEntity.ok(publishers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(false, "Error fetching publisher filter options.", "server_error", 500));
        }
    }

    @GetMapping("/select-options")
    @Operation(summary = "Get select options for publishers")
    public ResponseEntity<?> selectOptions() {
        SelectOptions options = publisherService.getSelectOptions();

        boolean hasOptions = !options.nationalities().isEmpty();

        if (hasOptions) {
            return ResponseEntity.ok(options);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiErrorResponse(false, "No select options found.", "no_content", 204));
        }
    }
}
