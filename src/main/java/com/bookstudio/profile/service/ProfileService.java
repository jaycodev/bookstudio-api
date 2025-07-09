package com.bookstudio.profile.service;

import com.bookstudio.auth.util.PasswordUtils;
import com.bookstudio.user.model.User;
import com.bookstudio.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

	private final UserRepository userRepository;

	@Transactional
	public Optional<User> updateProfile(Long userId, String firstName, String lastName, String rawPassword) {
		return userRepository.findById(userId).map(user -> {
			user.setFirstName(firstName);
			user.setLastName(lastName);
			if (rawPassword != null && !rawPassword.trim().isEmpty()) {
				user.setPassword(PasswordUtils.hashPassword(rawPassword));
			}
			return userRepository.save(user);
		});
	}

    @Transactional
    public Optional<User> updateProfilePhoto(Long userId, byte[] newPhoto, boolean deletePhoto) {
        return userRepository.findById(userId).map(user -> {
            if (deletePhoto) {
                user.setProfilePhoto(null);
            } else if (newPhoto != null && newPhoto.length > 0) {
                user.setProfilePhoto(newPhoto);
            }
            return userRepository.save(user);
        });
    }

	public boolean validatePassword(Long userId, String rawPassword) {
		return userRepository.findById(userId)
				.map(user -> PasswordUtils.checkPassword(rawPassword, user.getPassword()))
				.orElse(false);
	}
}
