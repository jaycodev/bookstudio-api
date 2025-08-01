package com.bookstudio.loan.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bookstudio.copy.service.CopyService;
import com.bookstudio.loan.dto.LoanItemSummaryDto;
import com.bookstudio.loan.relation.LoanItem;
import com.bookstudio.loan.relation.LoanItemId;
import com.bookstudio.loan.repository.LoanItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanItemService {

    private final LoanItemRepository loanItemRepository;

    private final CopyService copyService;

    public List<LoanItem> getForSelect() {
        return loanItemRepository.findAllByOrderByIdDesc();
    }

    public Optional<LoanItem> findById(LoanItemId loanItemId) {
        return loanItemRepository.findById(loanItemId);
    }

    public LoanItemSummaryDto toSummaryDto(LoanItem loanItem) {
        return LoanItemSummaryDto.builder()
                .copy(copyService.toSummaryDto(loanItem.getCopy()))
                .dueDate(loanItem.getDueDate())
                .returnDate(loanItem.getReturnDate())
                .status(loanItem.getStatus())
                .build();
    }

}
