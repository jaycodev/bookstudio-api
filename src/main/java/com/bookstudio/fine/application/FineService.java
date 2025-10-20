package com.bookstudio.fine.application;

import com.bookstudio.fine.application.dto.request.CreateFineRequest;
import com.bookstudio.fine.application.dto.request.UpdateFineRequest;
import com.bookstudio.fine.application.dto.response.FineDetailResponse;
import com.bookstudio.fine.application.dto.response.FineListResponse;
import com.bookstudio.fine.domain.model.Fine;
import com.bookstudio.fine.infrastructure.repository.FineRepository;
import com.bookstudio.loan.application.LoanItemService;
import com.bookstudio.shared.exception.ResourceNotFoundException;
import com.bookstudio.shared.response.OptionResponse;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FineService {
    @PersistenceContext
    private EntityManager entityManager;

    private final FineRepository fineRepository;

    private final LoanItemService loanItemService;

    public List<FineListResponse> getList() {
        return fineRepository.findList();
    }

    public List<OptionResponse> getOptions() {
        return fineRepository.findForOptions();
    }

    public Optional<Fine> findById(Long id) {
        return fineRepository.findById(id);
    }

    public FineDetailResponse getDetailById(Long id) {
        return fineRepository.findDetailById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fine not found with ID: " + id));
    }

    @Transactional
    public FineListResponse create(CreateFineRequest request) {
        Fine fine = new Fine();
        fine.setLoanItem(loanItemService.findById(request.loanItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Loan item not found with ID: " + request.loanItemId())));

        fine.setAmount(request.amount());
        fine.setDaysLate(request.daysLate());
        fine.setStatus(request.status());
        fine.setIssuedAt(request.issuedAt());

        Fine saved = fineRepository.save(fine);
        entityManager.refresh(saved);

        return toListResponse(saved);
    }

    @Transactional
    public FineListResponse update(Long id, UpdateFineRequest request) {
        Fine fine = fineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fine not found with ID: " + id));

        fine.setAmount(request.amount());
        fine.setDaysLate(request.daysLate());
        fine.setStatus(request.status());

        Fine updated = fineRepository.save(fine);

        return toListResponse(updated);
    }

    private FineListResponse toListResponse(Fine fine) {
        return new FineListResponse(
                fine.getId(),
                fine.getCode(),

                fine.getLoanItem().getLoan().getId(),
                fine.getLoanItem().getLoan().getCode(),

                fine.getLoanItem().getCopy().getId(),
                fine.getLoanItem().getCopy().getCode(),

                fine.getAmount(),
                fine.getDaysLate(),
                fine.getIssuedAt(),
                fine.getStatus());
    }
}
