package com.bookstudio.location.service;

import com.bookstudio.location.model.Shelf;
import com.bookstudio.location.repository.ShelfRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShelfService {

    private final ShelfRepository shelfRepository;

    public List<Shelf> getForSelect() {
        return shelfRepository.findAllByOrderByShelfIdDesc();
    }

    public Optional<Shelf> findById(Long shelfId) {
        return shelfRepository.findById(shelfId);
    }
}
