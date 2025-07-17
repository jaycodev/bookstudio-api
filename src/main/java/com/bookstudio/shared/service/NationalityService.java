package com.bookstudio.shared.service;

import com.bookstudio.shared.model.Nationality;
import com.bookstudio.shared.repository.NationalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NationalityService {

    private final NationalityRepository nationalityRepository;

    public List<Nationality> getForSelect() {
        return nationalityRepository.findAllByOrderByNameAsc();
    }

    public Optional<Nationality> findById(Long nationalityid) {
        return nationalityRepository.findById(nationalityid);
    }
}
