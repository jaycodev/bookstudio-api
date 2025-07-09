package com.bookstudio.user.service;

import com.bookstudio.auth.util.PasswordUtils;
import com.bookstudio.user.dto.CreateUserDto;
import com.bookstudio.user.dto.UpdateUserDto;
import com.bookstudio.user.dto.UserResponseDto;
import com.bookstudio.user.model.User;
import com.bookstudio.user.projection.UserViewProjection;
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

    public List<UserViewProjection> getList(Long loggedUserId) {
        return userRepository.findList(loggedUserId);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<UserViewProjection> getInfoById(Long userId) {
        return userRepository.findInfoById(userId);
    }

    @Transactional
    public UserResponseDto create(CreateUserDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ingresado ya ha sido registrado.");
        }

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ingresado ya ha sido registrado.");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(PasswordUtils.hashPassword(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setProfilePhoto(dto.getProfilePhoto());

        User saved = userRepository.save(user);

        return new UserResponseDto(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getRole().name(),
                saved.getProfilePhoto());
    }

    @Transactional
    public UserResponseDto update(UpdateUserDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getUserId()));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(dto.getRole());

        if (Boolean.TRUE.equals(dto.getDeletePhoto())) {
            user.setProfilePhoto(null);
        } else if (dto.getProfilePhoto() != null && dto.getProfilePhoto().length > 0) {
            user.setProfilePhoto(dto.getProfilePhoto());
        }

        User saved = userRepository.save(user);

        return new UserResponseDto(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getRole().name(),
                saved.getProfilePhoto());
    }

    @Transactional
    public boolean delete(Long userId) {
        if (!userRepository.existsById(userId))
            return false;
        userRepository.deleteById(userId);
        return true;
    }
}
