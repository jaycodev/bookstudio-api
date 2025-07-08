package com.bookstudio.user.service;

import com.bookstudio.user.model.User;
import com.bookstudio.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public List<User> listUsersExcept(Long loggedUserId) {
		return userRepository.findByIdNot(loggedUserId);
	}

	public Optional<User> getUser(Long id) {
		return userRepository.findById(id);
	}

	@Transactional
	public User createUser(User user) {
		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			throw new RuntimeException("El nombre de usuario ingresado ya ha sido registrado.");
		}

		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("El correo electrónico ingresado ya ha sido registrado.");
		}

		return userRepository.save(user);
	}

	@Transactional
	public User updateUser(Long id, User updatedData) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		user.setFirstName(updatedData.getFirstName());
		user.setLastName(updatedData.getLastName());
		user.setRole(updatedData.getRole());
		user.setProfilePhoto(updatedData.getProfilePhoto());

		return userRepository.save(user);
	}

	@Transactional
	public boolean deleteUser(Long id) {
		if (!userRepository.existsById(id)) return false;
		userRepository.deleteById(id);
		return true;
	}
}
