package com.bookstudio.shared.service;

import com.bookstudio.shared.model.Faculty;
import com.bookstudio.shared.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public List<Faculty> getForSelect() {
        return facultyRepository.findAll();
    }

    public Optional<Faculty> findById(Long facultyId) {
        return facultyRepository.findById(facultyId);
    }
}
