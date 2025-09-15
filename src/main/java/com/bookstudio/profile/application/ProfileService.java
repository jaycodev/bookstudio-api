package com.bookstudio.profile.application;

import com.bookstudio.auth.util.PasswordUtils;
import com.bookstudio.worker.domain.model.Worker;
import com.bookstudio.worker.infrastructure.repository.WorkerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

	private final WorkerRepository userRepository;

	@Transactional
	public Optional<Worker> updateProfile(Long loggedUserId, String firstName, String lastName, String rawPassword) {
		return userRepository.findById(loggedUserId).map(user -> {
			user.setFirstName(firstName);
			user.setLastName(lastName);
			if (rawPassword != null && !rawPassword.trim().isEmpty()) {
				user.setPassword(PasswordUtils.hashPassword(rawPassword));
			}
			return userRepository.save(user);
		});
	}

    @Transactional
    public Optional<Worker> updatePhoto(Long userId, String newPhoto, boolean deletePhoto) {
        return userRepository.findById(userId).map(user -> {
            if (deletePhoto) {
                user.setProfilePhotoUrl(null);
            } else if (newPhoto != null && !newPhoto.isBlank()) {
                user.setProfilePhotoUrl(newPhoto);
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
