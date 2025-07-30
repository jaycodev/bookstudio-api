package com.bookstudio.fine.service;

import com.bookstudio.fine.dto.FineResponseDto;
import com.bookstudio.fine.dto.CreateFineDto;
import com.bookstudio.fine.dto.UpdateFineDto;
import com.bookstudio.fine.model.Fine;
import com.bookstudio.fine.projection.FineInfoProjection;
import com.bookstudio.fine.projection.FineListProjection;
import com.bookstudio.fine.projection.FineSelectProjection;
import com.bookstudio.fine.repository.FineRepository;
import com.bookstudio.shared.service.LoanItemService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FineService {

    private final FineRepository fineRepository;

    private final LoanItemService loanItemService;

    public List<FineListProjection> getList() {
        return fineRepository.findList();
    }

    public Optional<Fine> findById(Long fineId) {
        return fineRepository.findById(fineId);
    }

    public Optional<FineInfoProjection> getInfoById(Long fineId) {
        return fineRepository.findInfoById(fineId);
    }

    @Transactional
    public FineResponseDto create(CreateFineDto dto) {
        Fine fine = new Fine();
        fine.setAmount(dto.getAmount());
        fine.setDaysLate(dto.getDaysLate());
        fine.setStatus(dto.getStatus());
        fine.setIssuedAt(dto.getIssuedAt());

        fine.setLoanItem(loanItemService.findById(dto.getLoanItemId())
                .orElseThrow(() -> new RuntimeException("Loan item not found")));

        Fine saved = fineRepository.save(fine);

        return new FineResponseDto(
                saved.getFineId(),
                saved.getCode(),
                saved.getLoanItem().getLoan().getCode(),
                saved.getAmount(),
                saved.getDaysLate(),
                saved.getStatus().name(),
                saved.getIssuedAt());
    }

    @Transactional
    public FineResponseDto update(UpdateFineDto dto) {
        Fine fine = fineRepository.findById(dto.getFineId())
                .orElseThrow(() -> new RuntimeException("Multa no encontrada con ID: " + dto.getFineId()));

        fine.setAmount(dto.getAmount());
        fine.setDaysLate(dto.getDaysLate());
        fine.setStatus(dto.getStatus());

        Fine saved = fineRepository.save(fine);

        return new FineResponseDto(
                saved.getFineId(),
                saved.getCode(),
                saved.getLoanItem().getLoan().getCode(),
                saved.getAmount(),
                saved.getDaysLate(),
                saved.getStatus().name(),
                saved.getIssuedAt());
    }

    public List<FineSelectProjection> getForSelect() {
        return fineRepository.findForSelect();
    }
}
