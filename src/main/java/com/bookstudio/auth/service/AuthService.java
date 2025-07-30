package com.bookstudio.auth.service;

import com.bookstudio.auth.util.PasswordUtils;
import com.bookstudio.worker.model.Worker;
import com.bookstudio.worker.repository.WorkerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final WorkerRepository userRepository;

    public Optional<Worker> verifyLogin(String username, String plainPassword) {
        return userRepository.findByUsername(username)
                .filter(user -> PasswordUtils.checkPassword(plainPassword, user.getPassword()));
    }
}
