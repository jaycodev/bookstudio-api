package com.bookstudio.auth.controller;

import com.bookstudio.auth.service.AuthService;
import com.bookstudio.auth.service.EmailService;
import com.bookstudio.auth.service.PasswordResetService;
import com.bookstudio.auth.util.LoginConstants;
import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;
import com.bookstudio.user.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;
    private final EmailService emailService;

    private static final int SESSION_TIMEOUT_MINUTES = 15;
    private static final int MAX_ATTEMPTS = 5;
    private static final int BLOCK_DURATION_MINUTES = 3;
    private static final long EXPIRATION_TIME = 30 * 60 * 1000;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request,
            HttpServletResponse response) {
        Cookie blockTimeCookie = getCookie(request, "blockTime");
        Cookie attemptsCookie = getCookie(request, "loginAttempts");

        if (blockTimeCookie != null) {
            try {
                long blockTimeValue = Long.parseLong(blockTimeCookie.getValue());
                if (System.currentTimeMillis() < blockTimeValue) {
                    long remainingSeconds = (blockTimeValue - System.currentTimeMillis()) / 1000;
                    return ResponseEntity.ok().body(
                            "{\"success\": false, \"message\": \"Demasiados intentos fallidos. Intenta nuevamente en "
                                    + remainingSeconds + " segundos.\"}");
                } else {
                    clearCookie(blockTimeCookie, response);
                    if (attemptsCookie != null)
                        clearCookie(attemptsCookie, response);
                }
            } catch (NumberFormatException e) {
                clearCookie(blockTimeCookie, response);
            }
        }

        Optional<User> optionalUser = authService.verifyLogin(username, password);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (blockTimeCookie != null)
                clearCookie(blockTimeCookie, response);
            if (attemptsCookie != null)
                clearCookie(attemptsCookie, response);

            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(SESSION_TIMEOUT_MINUTES * 60);

            session.setAttribute("user", user);
            session.setAttribute(LoginConstants.ID, String.valueOf(user.getId()));
            session.setAttribute(LoginConstants.USERNAME, user.getUsername());
            session.setAttribute(LoginConstants.FIRSTNAME, user.getFirstName());
            session.setAttribute(LoginConstants.LASTNAME, user.getLastName());
            session.setAttribute(LoginConstants.EMAIL, user.getEmail());
            session.setAttribute(LoginConstants.ROLE, user.getRole().name());

            String photoUrl = user.getProfilePhotoUrl();
            if (photoUrl != null && !photoUrl.isBlank()) {
                session.setAttribute(LoginConstants.USER_PROFILE_IMAGE, photoUrl);
            } else {
                session.removeAttribute(LoginConstants.USER_PROFILE_IMAGE);
            }

            return ResponseEntity.ok("{\"success\": true, \"message\": \"Inicio de sesión exitoso.\"}");
        } else {
            int attempts = 0;
            if (attemptsCookie != null) {
                try {
                    attempts = Integer.parseInt(attemptsCookie.getValue());
                } catch (NumberFormatException e) {
                    attempts = 0;
                }
            }
            attempts++;

            Cookie newAttempts = new Cookie("loginAttempts", String.valueOf(attempts));
            newAttempts.setMaxAge(60 * 60);
            newAttempts.setPath("/");
            response.addCookie(newAttempts);

            if (attempts >= MAX_ATTEMPTS) {
                long lockUntil = System.currentTimeMillis() + (BLOCK_DURATION_MINUTES * 60 * 1000);
                Cookie newBlock = new Cookie("blockTime", String.valueOf(lockUntil));
                newBlock.setMaxAge(BLOCK_DURATION_MINUTES * 60);
                newBlock.setPath("/");
                response.addCookie(newBlock);

                return ResponseEntity.ok(
                        "{\"success\": false, \"message\": \"Demasiados intentos fallidos. Estás bloqueado por "
                                + BLOCK_DURATION_MINUTES + " minutos.\"}");
            } else {
                return ResponseEntity.ok(
                        "{\"success\": false, \"message\": \"Usuario y/o contraseña incorrectos.\"}");
            }
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok("{\"success\": true, \"message\": \"Sesión cerrada.\"}");
    }

    @GetMapping("/keep-alive")
    public ResponseEntity<?> keepSessionAlive(HttpServletRequest request) {
        request.getSession().setMaxInactiveInterval(SESSION_TIMEOUT_MINUTES * 60);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email, HttpServletRequest request) {
        if (!passwordResetService.emailExists(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiError(false,
                            "La dirección de correo electrónico ingresada no está asociada a ninguna cuenta.",
                            "invalid_email", 400));
        }

        String token = UUID.randomUUID().toString();
        long expiryTime = System.currentTimeMillis() + EXPIRATION_TIME;

        boolean tokenSaved = passwordResetService.saveToken(email, token, expiryTime);
        if (!tokenSaved) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(false, "No se pudo generar el enlace.", "token_generation_error", 500));
        }

        boolean emailSent = sendResetEmail(email, token, request);
        if (emailSent) {
            return ResponseEntity.ok(new ApiResponse(true));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(false, "Ocurrió un error al enviar el correo.", "email_error", 500));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword,
            HttpServletRequest request) {
        if (!passwordResetService.isTokenValid(token)) {
            return ResponseEntity.badRequest().body("""
                    {
                        "success": false,
                        "message": "El enlace de restablecimiento ha expirado o es inválido."
                    }
                    """);
        }

        boolean updated = passwordResetService.updatePassword(token, newPassword);
        if (!updated) {
            return ResponseEntity.internalServerError().body("""
                    {
                        "success": false,
                        "message": "Ocurrió un error al actualizar la contraseña."
                    }
                    """);
        }

        passwordResetService.getEmailByToken(token).ifPresent(email -> {
            boolean sent = sendPasswordChangeEmail(email);
            if (!sent) {
                System.err.println("No se pudo enviar el correo de cambio de contraseña.");
            }
        });

        return ResponseEntity.ok("""
                {
                    "success": true
                }
                """);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Map<String, Boolean>> validateToken(@RequestParam String token) {
        boolean isValid = passwordResetService.isTokenValid(token);
        return ResponseEntity.ok(Map.of("valid", isValid));
    }

    private boolean sendResetEmail(String email, String token, HttpServletRequest request) {

        String resetLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + "/reset-password?token=" + token;

        String msg = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset='UTF-8'>
                    <style>
                        body { font-family: Arial; padding: 20px; background-color: #f4f4f4; }
                        .container { max-width: 600px; background: #fff; padding: 20px; border-radius: 10px; text-align: center; }
                        .title { font-size: 24px; font-weight: bold; }
                        .button { background-color: #000; color: #fff; padding: 12px 20px; border-radius: 8px; text-decoration: none; }
                        .link { display: block; margin-top: 10px; }
                        .footer { font-size: 12px; color: #777; margin-top: 20px; }
                    </style>
                </head>
                <body>
                    <div class='container'>
                        <p class='title'>¿Tienes problemas para acceder a tu cuenta de BookStudio?</p>
                        <p>Haz clic en el botón para restablecer tu contraseña. Este enlace es válido por 30 minutos.</p>
                        <a href='%s' class='button'>Restablecer contraseña</a>
                        <a href='%s' class='link'>%s</a>
                        <p class='footer'>Saludos,<br/>El equipo de BookStudio</p>
                    </div>
                </body>
                </html>
                """
                .formatted(resetLink, resetLink, resetLink);

        return emailService.sendHtmlEmail(email, "Restablecer tu contraseña", msg);
    }

    private boolean sendPasswordChangeEmail(String email) {
        String msg = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset='UTF-8'>
                    <style>
                        body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
                        .container { max-width: 600px; background: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
                        .title { font-size: 24px; font-weight: bold; color: #333; text-align: center; }
                        .content { font-size: 16px; color: #555; line-height: 1.5; text-align: center; }
                        .footer { font-size: 12px; color: #777; text-align: center; margin-top: 20px; }
                    </style>
                </head>
                <body>
                    <div class='container'>
                        <p class='title'>Se cambió tu contraseña</p>
                        <p class='content'>Se cambió tu contraseña, tal como lo pediste.</p>
                        <p class='content'>Si no realizaste este cambio, contacta con nuestro soporte inmediatamente.</p>
                        <p class='footer'>Saludos,<br/>El equipo de BookStudio</p>
                    </div>
                </body>
                </html>
                """;

        return emailService.sendHtmlEmail(email, "Cambio de contraseña", msg);
    }

    private Cookie getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null)
            return null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name))
                return cookie;
        }
        return null;
    }

    private void clearCookie(Cookie cookie, HttpServletResponse response) {
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
