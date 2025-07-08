package com.bookstudio.shared.service;

import com.bookstudio.shared.model.Faculty;
import com.bookstudio.shared.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public List<Faculty> getFacultiesForSelect() {
        return facultyRepository.findAll();
    }
}
