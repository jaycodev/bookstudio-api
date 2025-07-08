package com.bookstudio.shared.service;

import com.bookstudio.shared.model.Nationality;
import com.bookstudio.shared.repository.NationalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NationalityService {

    private final NationalityRepository nationalityRepository;

    public List<Nationality> getNationalitiesForSelect() {
        return nationalityRepository.findAll();
    }
}
