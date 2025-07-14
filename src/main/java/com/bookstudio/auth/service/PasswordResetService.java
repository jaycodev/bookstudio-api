package com.bookstudio.auth.service;

import com.bookstudio.auth.model.PasswordResetToken;
import com.bookstudio.auth.repository.PasswordResetTokenRepository;
import com.bookstudio.auth.util.PasswordUtils;
import com.bookstudio.user.model.User;
import com.bookstudio.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepo;
    private final UserRepository userRepo;

    public boolean emailExists(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    public boolean saveToken(String email, String token, long expiryMillis) {
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .email(email)
                .token(token)
                .expiryTime(new Date(expiryMillis))
                .build();
        tokenRepo.save(resetToken);
        return true;
    }

    public boolean isTokenValid(String token) {
        return tokenRepo.findByToken(token)
                .map(t -> new Date().before(t.getExpiryTime()))
                .orElse(false);
    }

    public boolean updatePassword(String token, String newPassword) {
        Optional<PasswordResetToken> optionalToken = tokenRepo.findByToken(token);
        if (optionalToken.isEmpty()) return false;

        PasswordResetToken resetToken = optionalToken.get();
        if (new Date().after(resetToken.getExpiryTime())) return false;

        Optional<User> optionalUser = userRepo.findByEmail(resetToken.getEmail());
        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();
        user.setPassword(PasswordUtils.hashPassword(newPassword));
        userRepo.save(user);

        tokenRepo.deleteByToken(token);
        return true;
    }

    public Optional<String> getEmailByToken(String token) {
        return tokenRepo.findByToken(token).map(PasswordResetToken::getEmail);
    }
}
