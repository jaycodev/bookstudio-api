package com.bookstudio.publisher.controller;

import com.bookstudio.publisher.model.Publisher;
import com.bookstudio.publisher.service.PublisherService;
import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;
import com.bookstudio.shared.util.SelectOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
public class PublisherController {

	private final PublisherService publisherService;

	@GetMapping
	public ResponseEntity<?> listPublishers() {
		List<Publisher> publishers = publisherService.listPublishers();
		if (publishers.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(new ApiError(false, "No publishers found.", "no_content", 204));
		}
		return ResponseEntity.ok(publishers);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getPublisher(@PathVariable Long id) {
		Publisher publisher = publisherService.getPublisher(id).orElse(null);
		if (publisher == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiError(false, "Publisher not found.", "not_found", 404));
		}
		if (publisher.getPhoto() != null) {
			publisher.setPhotoBase64("data:image/jpeg;base64," +
					Base64.getEncoder().encodeToString(publisher.getPhoto()));
			publisher.setPhoto(null);
		}
		return ResponseEntity.ok(publisher);
	}

	@PostMapping
	public ResponseEntity<?> createPublisher(@ModelAttribute Publisher publisher) {
		try {
			Publisher created = publisherService.createPublisher(publisher);
			if (created.getPhoto() != null) {
				created.setPhotoBase64("data:image/jpeg;base64," +
						Base64.getEncoder().encodeToString(created.getPhoto()));
			}
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ApiResponse(true, created));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ApiError(false, e.getMessage(), "creation_failed", 400));
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updatePublisher(@PathVariable Long id, @ModelAttribute Publisher updatedData) {
		try {
			Publisher updated = publisherService.updatePublisher(id, updatedData);
			if (updated.getPhoto() != null) {
				updated.setPhotoBase64("data:image/jpeg;base64," +
						Base64.getEncoder().encodeToString(updated.getPhoto()));
				updated.setPhoto(null);
			}
			return ResponseEntity.ok(new ApiResponse(true, updated));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiError(false, e.getMessage(), "update_failed", 404));
		}
	}

	@GetMapping("/select-options")
	public ResponseEntity<?> getSelectOptions() {
		try {
			SelectOptions options = publisherService.populateSelects();
			if ((options.getNationalities() != null && !options.getNationalities().isEmpty()) ||
				(options.getLiteraryGenres() != null && !options.getLiteraryGenres().isEmpty())) {
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
