package com.bookstudio.location.application;

import com.bookstudio.location.domain.model.Shelf;
import com.bookstudio.location.infrastructure.repository.ShelfRepository;
import com.bookstudio.shared.application.dto.response.OptionResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShelfService {

    private final ShelfRepository shelfRepository;

    public List<OptionResponse> getOptions() {
        return shelfRepository.findForOptions();
    }

    public Optional<Shelf> findById(Long id) {
        return shelfRepository.findById(id);
    }
}
