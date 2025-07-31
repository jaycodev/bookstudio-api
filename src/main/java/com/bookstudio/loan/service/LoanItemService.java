package com.bookstudio.loan.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bookstudio.copy.service.CopyService;
import com.bookstudio.loan.dto.LoanItemDto;
import com.bookstudio.loan.relation.LoanItem;
import com.bookstudio.loan.relation.LoanItemId;
import com.bookstudio.loan.repository.LoanItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanItemService {

    private final LoanItemRepository loanItemRepository;

    private final LoanService loanService;
    private final CopyService copyService;

    public List<LoanItem> getForSelect() {
        return loanItemRepository.findAllByOrderByIdDesc();
    }

    public Optional<LoanItem> findById(LoanItemId loanItemId) {
        return loanItemRepository.findById(loanItemId);
    }

    public LoanItemDto toDto(LoanItem loanItem) {
        return LoanItemDto.builder()
                .id(loanItem.getId())
                .loan(loanService.toDto(loanItem.getLoan()))
                .copy(copyService.toDto(loanItem.getCopy()))
                .dueDate(loanItem.getDueDate())
                .returnDate(loanItem.getReturnDate())
                .status(loanItem.getStatus().name())
                .build();
    }
}
