package com.bookstudio.auth.service;

import com.bookstudio.auth.util.PasswordUtils;
import com.bookstudio.user.model.User;
import com.bookstudio.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public Optional<User> verifyLogin(String username, String plainPassword) {
        return userRepository.findByUsername(username)
                .filter(user -> PasswordUtils.checkPassword(plainPassword, user.getPassword()));
    }
}
