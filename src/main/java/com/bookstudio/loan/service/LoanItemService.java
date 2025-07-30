package com.bookstudio.loan.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bookstudio.loan.relation.LoanItem;
import com.bookstudio.loan.relation.LoanItemId;
import com.bookstudio.loan.repository.LoanItemRepository;

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
