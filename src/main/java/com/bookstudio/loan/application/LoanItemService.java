package com.bookstudio.loan.application;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstudio.loan.domain.model.LoanItem;
import com.bookstudio.loan.domain.model.LoanItemId;
import com.bookstudio.loan.infrastructure.repository.LoanItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoanItemService {
    private final LoanItemRepository loanItemRepository;

    public Optional<LoanItem> findById(LoanItemId id) {
        return loanItemRepository.findById(id);
    }
}
