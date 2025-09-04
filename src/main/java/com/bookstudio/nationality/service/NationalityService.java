package com.bookstudio.nationality.service;

import com.bookstudio.nationality.dto.NationalityOptionDto;
import com.bookstudio.nationality.dto.NationalitySummaryDto;
import com.bookstudio.nationality.model.Nationality;
import com.bookstudio.nationality.repository.NationalityRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NationalityService {

    private final NationalityRepository nationalityRepository;

    public List<NationalityOptionDto> getOptions() {
        return nationalityRepository.findForOptions();
    }

    public Optional<Nationality> findById(Long nationalityid) {
        return nationalityRepository.findById(nationalityid);
    }

    public NationalitySummaryDto toSummaryDto(Nationality nationality) {
        return NationalitySummaryDto.builder()
                .id(nationality.getNationalityId())
                .name(nationality.getName())
                .build();
    }
}
