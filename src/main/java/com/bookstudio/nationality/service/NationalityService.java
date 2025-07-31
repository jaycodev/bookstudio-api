package com.bookstudio.nationality.service;

import com.bookstudio.nationality.dto.NationalityDto;
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

    public List<Nationality> getForSelect() {
        return nationalityRepository.findAllByOrderByNameAsc();
    }

    public Optional<Nationality> findById(Long nationalityid) {
        return nationalityRepository.findById(nationalityid);
    }

    public NationalityDto toDto(Nationality nationality) {
        return NationalityDto.builder()
                .id(nationality.getNationalityId())
                .name(nationality.getName())
                .build();
    }
}
