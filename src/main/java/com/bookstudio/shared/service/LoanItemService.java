package com.bookstudio.shared.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bookstudio.shared.model.LoanItem;
import com.bookstudio.shared.model.LoanItemId;
import com.bookstudio.shared.repository.LoanItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanItemService {

    private final LoanItemRepository loanItemRepository;

    public List<LoanItem> getForSelect() {
        return loanItemRepository.findAllByOrderByIdDesc();
    }

    public Optional<LoanItem> findById(LoanItemId loanItemId) {
        return loanItemRepository.findById(loanItemId);
    }
}
