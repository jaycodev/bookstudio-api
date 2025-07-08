package com.bookstudio.user.controller;

import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;
import com.bookstudio.shared.util.ValidationErrorResponse;
import com.bookstudio.shared.util.FieldErrorDetail;
import com.bookstudio.user.model.User;
import com.bookstudio.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public ResponseEntity<?> listUsers(@RequestParam(required = false) Long excludeId) {
		List<User> users = (excludeId != null)
				? userService.listUsersExcept(excludeId)
				: userService.listUsersExcept(-1L);

		if (users.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(new ApiError(false, "No users found.", "no_content", 204));
		}
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUser(@PathVariable Long id) {
		Optional<User> optionalUser = userService.getUser(id);

		if (optionalUser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiError(false, "User not found.", "not_found", 404));
		}

		User user = optionalUser.get();
		if (user.getProfilePhoto() != null) {
			user.setProfilePhotoBase64("data:image/jpeg;base64," +
					Base64.getEncoder().encodeToString(user.getProfilePhoto()));
			user.setProfilePhoto(null);
		}
		return ResponseEntity.ok(user);
	}


	@PostMapping
	public ResponseEntity<?> createUser(@ModelAttribute User user) {
		try {
			User created = userService.createUser(user);
			return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
		} catch (RuntimeException e) {
			return handleFieldError(e.getMessage(), "addUserEmail", "addUserUsername");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiError(false, "Server error while creating user.", "server_error", 500));
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @ModelAttribute User updatedData) {
		try {
			User updated = userService.updateUser(id, updatedData);
			if (updated.getProfilePhoto() != null) {
				updated.setProfilePhotoBase64("data:image/jpeg;base64," +
						Base64.getEncoder().encodeToString(updated.getProfilePhoto()));
				updated.setProfilePhoto(null);
			}
			return ResponseEntity.ok(new ApiResponse(true, updated));
		} catch (RuntimeException e) {
			return handleFieldError(e.getMessage(), "editUserEmail", "editUserUsername");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiError(false, "Server error while updating user.", "server_error", 500));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		boolean deleted = userService.deleteUser(id);
		if (deleted) {
			return ResponseEntity.ok(new ApiResponse(true));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiError(false, "User not found.", "deletion_failed", 404));
		}
	}

	private ResponseEntity<?> handleFieldError(String message, String emailField, String usernameField) {
		String lower = message.toLowerCase();
		if (lower.contains("correo electrónico") && emailField != null) {
			return ResponseEntity.badRequest().body(new ValidationErrorResponse(
					false, "validation_error", 400,
					List.of(new FieldErrorDetail(emailField, message))
			));
		} else if (lower.contains("usuario") && usernameField != null) {
			return ResponseEntity.badRequest().body(new ValidationErrorResponse(
					false, "validation_error", 400,
					List.of(new FieldErrorDetail(usernameField, message))
			));
		} else {
			return ResponseEntity.badRequest().body(new ApiError(false, message, "validation_error", 400));
		}
	}
}
