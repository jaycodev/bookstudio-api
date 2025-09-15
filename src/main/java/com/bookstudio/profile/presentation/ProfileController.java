package com.bookstudio.profile.presentation;

import com.bookstudio.auth.util.LoginConstants;
import com.bookstudio.profile.application.ProfileService;
import com.bookstudio.shared.domain.dto.response.ApiErrorResponse;
import com.bookstudio.shared.domain.dto.response.ApiResponse;
import com.bookstudio.worker.domain.model.Worker;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(
            HttpSession session,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam(required = false) String password) {

        Object loggedIdObj = session.getAttribute(LoginConstants.ID);
        if (loggedIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiErrorResponse(false, "No user session.", "unauthorized", 401));
        }

        try {
            Long loggedId = Long.valueOf(loggedIdObj.toString());
            Optional<Worker> updated = profileService.updateProfile(loggedId, firstName, lastName, password);

            if (updated.isPresent()) {
                Worker user = updated.get();
                session.setAttribute(LoginConstants.FIRSTNAME, user.getFirstName());
                session.setAttribute(LoginConstants.LASTNAME, user.getLastName());
                return ResponseEntity.ok(new ApiResponse(true, "Perfil actualizado con éxito."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiErrorResponse(false, "No se pudo actualizar el perfil.", "update_failed", 400));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(false, e.getMessage(), "server_error", 500));
        }
    }

    @PostMapping("/update-photo")
    public ResponseEntity<?> updatePhoto(
            HttpSession session,
            @RequestParam(required = false) String photoUrl,
            @RequestParam(defaultValue = "false") boolean deletePhoto) {

        Object loggedIdObj = session.getAttribute(LoginConstants.ID);
        if (loggedIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiErrorResponse(false, "No user session.", "unauthorized", 401));
        }

        try {
            Long loggedId = Long.valueOf(loggedIdObj.toString());
            Optional<Worker> updated = profileService.updatePhoto(loggedId, photoUrl, deletePhoto);

            if (updated.isPresent()) {
                Worker user = updated.get();

                if (user.getProfilePhotoUrl() != null && !user.getProfilePhotoUrl().isBlank()) {
                    session.setAttribute(LoginConstants.USER_PROFILE_IMAGE, user.getProfilePhotoUrl());
                } else {
                    session.removeAttribute(LoginConstants.USER_PROFILE_IMAGE);
                }

                return ResponseEntity.ok(new ApiResponse(true, "Foto actualizada con éxito."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiErrorResponse(false, "No se pudo actualizar la foto.", "update_failed", 400));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(false, e.getMessage(), "server_error", 500));
        }
    }

    @GetMapping("/validate-password")
    public ResponseEntity<?> validatePassword(
            HttpSession session,
            @RequestParam String currentPassword) {
                
        Object loggedIdObj = session.getAttribute(LoginConstants.ID);
        if (loggedIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiErrorResponse(false, "No user session.", "unauthorized", 401));
        }

        Long loggedId = Long.valueOf(loggedIdObj.toString());
        boolean isValid = profileService.validatePassword(loggedId, currentPassword);

        if (isValid) {
            return ResponseEntity.ok(new ApiResponse(true));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponse(false, "La contraseña actual no es correcta.", "invalid_password", 400));
        }
    }
}
