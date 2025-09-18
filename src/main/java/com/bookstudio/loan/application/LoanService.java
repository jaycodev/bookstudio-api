package com.bookstudio.loan.application;

import com.bookstudio.book.application.BookService;
import com.bookstudio.copy.application.CopyService;
import com.bookstudio.copy.domain.model.Copy;
import com.bookstudio.loan.application.dto.request.CreateLoanItemRequest;
import com.bookstudio.loan.application.dto.request.CreateLoanRequest;
import com.bookstudio.loan.application.dto.request.UpdateLoanItemRequest;
import com.bookstudio.loan.application.dto.request.UpdateLoanRequest;
import com.bookstudio.loan.application.dto.response.LoanDetailResponse;
import com.bookstudio.loan.application.dto.response.LoanListResponse;
import com.bookstudio.loan.domain.model.Loan;
import com.bookstudio.loan.domain.model.LoanItem;
import com.bookstudio.loan.domain.model.LoanItemId;
import com.bookstudio.loan.domain.model.type.LoanItemStatus;
import com.bookstudio.loan.infrastructure.repository.LoanItemRepository;
import com.bookstudio.loan.infrastructure.repository.LoanRepository;
import com.bookstudio.reader.application.ReaderService;
import com.bookstudio.shared.application.dto.response.OptionResponse;
import com.bookstudio.shared.util.SelectOptions;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoanService {

    @PersistenceContext
    private EntityManager entityManager;

    private final LoanRepository loanRepository;
    private final LoanItemRepository loanItemRepository;

    private final BookService bookService;
    private final ReaderService readerService;
    private final CopyService copyService;

    public List<LoanListResponse> getList() {
        return loanRepository.findList();
    }

    public List<OptionResponse> getOptions() {
        return loanRepository.findForOptions();
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .books(bookService.getOptions())
                .students(readerService.getOptions())
                .build();
    }

    public LoanDetailResponse getDetailById(Long id) {
        LoanDetailResponse base = loanRepository.findDetailById(id)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found with ID: " + id));

        return base.withItems(loanItemRepository.findLoanItemsByLoanId(id));
    }

    @Transactional
    public LoanListResponse create(CreateLoanRequest request) {
        Loan loan = new Loan();
        loan.setLoanDate(LocalDate.now());
        loan.setObservation(request.getObservation());

        loan.setReader(readerService.findById(request.getReaderId())
                .orElseThrow(() -> new EntityNotFoundException("Reader not found with ID: " + request.getReaderId())));

        Loan saved = loanRepository.save(loan);
        entityManager.refresh(saved);

        if (request.getItems() != null) {
            for (CreateLoanItemRequest itemDto : request.getItems()) {
                Copy copy = copyService.findById(itemDto.getCopyId())
                        .orElseThrow(
                                () -> new EntityNotFoundException("Copy not found with ID: " + itemDto.getCopyId()));

                LoanItem item = LoanItem.builder()
                        .id(new LoanItemId(saved.getId(), copy.getId()))
                        .loan(saved)
                        .copy(copy)
                        .dueDate(itemDto.getDueDate())
                        .status(LoanItemStatus.PRESTADO)
                        .build();

                loanItemRepository.save(item);
            }
        }

        return toListResponse(saved);
    }

    @Transactional
    public LoanListResponse update(Long id, UpdateLoanRequest request) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with ID: " + id));

        loan.setObservation(request.getObservation());

        loan.setReader(readerService.findById(request.getReaderId())
                .orElseThrow(() -> new EntityNotFoundException("Reader not found with ID: " + request.getReaderId())));

        Loan updated = loanRepository.save(loan);

        loanItemRepository.deleteAllByLoan(updated);

        if (request.getItems() != null) {
            for (UpdateLoanItemRequest itemDto : request.getItems()) {
                Copy copy = copyService.findById(itemDto.getCopyId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Copy not found with ID: " + itemDto.getCopyId()));

                LoanItem item = LoanItem.builder()
                        .id(new LoanItemId(updated.getId(), copy.getId()))
                        .loan(updated)
                        .copy(copy)
                        .dueDate(itemDto.getDueDate())
                        .status(itemDto.getStatus())
                        .build();

                loanItemRepository.save(item);
            }
        }

        return toListResponse(updated);
    }

    public LoanListResponse toListResponse(Loan loan) {
        Map<LoanItemStatus, Long> counts = loan.getLoanItems().stream()
                .collect(Collectors.groupingBy(
                        LoanItem::getStatus,
                        Collectors.counting()));

        return new LoanListResponse(
                loan.getId(),
                loan.getCode(),

                loan.getReader().getId(),
                loan.getReader().getCode(),
                loan.getReader().getFullName(),

                loan.getLoanDate(),

                counts.getOrDefault(LoanItemStatus.PRESTADO, 0L),
                counts.getOrDefault(LoanItemStatus.DEVUELTO, 0L),
                counts.getOrDefault(LoanItemStatus.RETRASADO, 0L),
                counts.getOrDefault(LoanItemStatus.EXTRAVIADO, 0L),
                counts.getOrDefault(LoanItemStatus.CANCELADO, 0L));
    }
}
