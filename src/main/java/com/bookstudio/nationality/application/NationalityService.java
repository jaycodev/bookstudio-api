package com.bookstudio.nationality.application;

import com.bookstudio.nationality.domain.model.Nationality;
import com.bookstudio.nationality.infrastructure.repository.NationalityRepository;
import com.bookstudio.shared.application.dto.response.OptionResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NationalityService {

    private final NationalityRepository nationalityRepository;

    public List<OptionResponse> getOptions() {
        return nationalityRepository.findForOptions();
    }

    public Optional<Nationality> findById(Long id) {
        return nationalityRepository.findById(id);
    }
}
