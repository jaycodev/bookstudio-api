package com.bookstudio.profile.controller;

import com.bookstudio.auth.util.LoginConstants;
import com.bookstudio.profile.service.ProfileService;
import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;
import com.bookstudio.user.model.User;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService profileService;

	@PostMapping("/update")
	public ResponseEntity<?> updateProfile(
			HttpSession session,
			@RequestParam String firstName,
			@RequestParam String lastName,
			@RequestParam(required = false) String password) {
		Long userId = (Long) session.getAttribute(LoginConstants.ID);
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiError(false, "No user session.", "unauthorized", 401));
		}

		try {
			Optional<User> updated = profileService.updateProfile(userId, firstName, lastName, password);
			if (updated.isPresent()) {
				User user = updated.get();
				session.setAttribute(LoginConstants.FIRSTNAME, user.getFirstName());
				session.setAttribute(LoginConstants.LASTNAME, user.getLastName());
				return ResponseEntity.ok(new ApiResponse(true, "Perfil actualizado con éxito."));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ApiError(false, "No se pudo actualizar el perfil.", "update_failed", 400));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiError(false, e.getMessage(), "server_error", 500));
		}
	}

	@PostMapping("/update-photo")
	public ResponseEntity<?> updateProfilePhoto(
			HttpSession session,
			@RequestParam(required = false) MultipartFile file,
			@RequestParam(defaultValue = "false") boolean deletePhoto) {
		Long userId = (Long) session.getAttribute(LoginConstants.ID);
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiError(false, "No user session.", "unauthorized", 401));
		}

		try {
			byte[] newPhoto = (file != null && !file.isEmpty()) ? file.getBytes() : null;

			Optional<User> updated = profileService.updateProfilePhoto(userId, newPhoto, deletePhoto);
			if (updated.isPresent()) {
				User user = updated.get();
				if (user.getProfilePhoto() != null) {
					String base64Image = Base64.getEncoder().encodeToString(user.getProfilePhoto());
					session.setAttribute(LoginConstants.USER_PROFILE_IMAGE, "data:image/jpeg;base64," + base64Image);
				} else {
					session.removeAttribute(LoginConstants.USER_PROFILE_IMAGE);
				}
				return ResponseEntity.ok(new ApiResponse(true, "Foto actualizada con éxito."));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ApiError(false, "No se pudo actualizar la foto.", "update_failed", 400));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiError(false, e.getMessage(), "server_error", 500));
		}
	}

	@GetMapping("/validate-password")
	public ResponseEntity<?> validatePassword(
			HttpSession session,
			@RequestParam String currentPassword) {
		Long userId = (Long) session.getAttribute(LoginConstants.ID);
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiError(false, "No user session.", "unauthorized", 401));
		}

		boolean isValid = profileService.validatePassword(userId, currentPassword);
		if (isValid) {
			return ResponseEntity.ok(new ApiResponse(true));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ApiError(false, "La contraseña actual no es correcta.", "invalid_password", 400));
		}
	}
}
