package com.bookstudio.user.controller;

import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;
import com.bookstudio.shared.util.ValidationErrorResponse;
import com.bookstudio.shared.util.FieldErrorDetail;
import com.bookstudio.user.dto.CreateUserDto;
import com.bookstudio.user.dto.UpdateUserDto;
import com.bookstudio.user.dto.UserResponseDto;
import com.bookstudio.user.projection.UserViewProjection;
import com.bookstudio.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public ResponseEntity<?> list(@RequestParam(required = false) Long excludeId) {
		List<UserViewProjection> users = (excludeId != null)
				? userService.getList(excludeId)
				: userService.getList(-1L);

		if (users.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(new ApiError(false, "No users found.", "no_content", 204));
		}
		return ResponseEntity.ok(users);
	}

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        UserViewProjection user = userService.getInfoById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, "User not found.", "not_found", 404));
        }
        return ResponseEntity.ok(user);
    }

	@PostMapping
	public ResponseEntity<?> create(@RequestBody CreateUserDto dto) {
		try {
			UserResponseDto created = userService.create(dto);
			return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
		} catch (RuntimeException e) {
			return handleFieldError(e.getMessage(), "addUserEmail", "addUserUsername");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiError(false, "Server error while creating user.", "server_error", 500));
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@RequestBody UpdateUserDto dto) {
		try {
			UserResponseDto updated = userService.update(dto);
			return ResponseEntity.ok(new ApiResponse(true, updated));
		} catch (RuntimeException e) {
			return handleFieldError(e.getMessage(), "editUserEmail", "editUserUsername");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiError(false, "Server error while updating user.", "server_error", 500));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		boolean deleted = userService.delete(id);
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
