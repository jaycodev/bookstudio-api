package com.bookstudio.fine.service;

import com.bookstudio.fine.dto.FineSelectDto;
import com.bookstudio.fine.dto.CreateFineDto;
import com.bookstudio.fine.dto.FineDto;
import com.bookstudio.fine.dto.FineListDto;
import com.bookstudio.fine.dto.UpdateFineDto;
import com.bookstudio.fine.model.Fine;
import com.bookstudio.fine.repository.FineRepository;
import com.bookstudio.loan.service.LoanItemService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FineService {

    @PersistenceContext
    private EntityManager entityManager;

    private final FineRepository fineRepository;

    private final LoanItemService loanItemService;

    public List<FineListDto> getList() {
        return fineRepository.findList();
    }

    public Optional<Fine> findById(Long fineId) {
        return fineRepository.findById(fineId);
    }

    public FineDto getInfoById(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new EntityNotFoundException("Fine not found with ID: " + fineId));
        return toDto(fine);
    }

    @Transactional
    public FineListDto create(CreateFineDto dto) {
        Fine fine = new Fine();
        fine.setAmount(dto.getAmount());
        fine.setDaysLate(dto.getDaysLate());
        fine.setStatus(dto.getStatus());
        fine.setIssuedAt(dto.getIssuedAt());

        fine.setLoanItem(loanItemService.findById(dto.getLoanItemId())
                .orElseThrow(() -> new RuntimeException("Loan item not found")));

        Fine saved = fineRepository.save(fine);
        entityManager.refresh(saved);

        return toListDto(saved);
    }

    @Transactional
    public FineListDto update(UpdateFineDto dto) {
        Fine fine = fineRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Fine not found with ID: " + dto.getId()));

        fine.setAmount(dto.getAmount());
        fine.setDaysLate(dto.getDaysLate());
        fine.setStatus(dto.getStatus());

        Fine saved = fineRepository.save(fine);

        return toListDto(saved);
    }

    public List<FineSelectDto> getForSelect() {
        return fineRepository.findForSelect();
    }

    public FineDto toDto(Fine fine) {
        return FineDto.builder()
                .id(fine.getFineId())
                .code(fine.getCode())
                .loanItem(loanItemService.toDto(fine.getLoanItem()))
                .amount(fine.getAmount())
                .daysLate(fine.getDaysLate())
                .status(fine.getStatus().name())
                .issuedAt(fine.getIssuedAt())
                .build();
    }

    private FineListDto toListDto(Fine fine) {
        return new FineListDto(
                fine.getCode(),
                fine.getLoanItem().getLoan().getCode(),
                fine.getAmount(),
                fine.getDaysLate(),
                fine.getStatus(),
                fine.getIssuedAt(),
                fine.getFineId());
    }
}
