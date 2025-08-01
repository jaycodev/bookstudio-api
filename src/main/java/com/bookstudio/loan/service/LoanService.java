package com.bookstudio.loan.service;

import com.bookstudio.book.service.BookService;
import com.bookstudio.copy.model.Copy;
import com.bookstudio.copy.service.CopyService;
import com.bookstudio.loan.dto.CreateLoanDto;
import com.bookstudio.loan.dto.CreateLoanItemDto;
import com.bookstudio.loan.dto.LoanDto;
import com.bookstudio.loan.dto.LoanItemDto;
import com.bookstudio.loan.dto.LoanListDto;
import com.bookstudio.loan.dto.UpdateLoanDto;
import com.bookstudio.loan.dto.UpdateLoanItemDto;
import com.bookstudio.loan.model.Loan;
import com.bookstudio.loan.relation.LoanItem;
import com.bookstudio.loan.relation.LoanItemId;
import com.bookstudio.loan.repository.LoanItemRepository;
import com.bookstudio.loan.repository.LoanRepository;
import com.bookstudio.reader.service.ReaderService;
import com.bookstudio.shared.enums.LoanItemStatus;
import com.bookstudio.shared.util.SelectOptions;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    @PersistenceContext
    private EntityManager entityManager;

    private final LoanRepository loanRepository;
    private final LoanItemRepository loanItemRepository;

    private final BookService bookService;
    private final ReaderService readerService;
    private final CopyService copyService;

    public List<LoanListDto> getList() {
        return loanRepository.findList();
    }

    public LoanDto getInfoById(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found with ID: " + loanId));
        return toDto(loan);
    }

    @Transactional
    public LoanListDto create(CreateLoanDto dto) {
        Loan loan = new Loan();
        loan.setLoanDate(LocalDate.now());
        loan.setObservation(dto.getObservation());

        loan.setReader(readerService.findById(dto.getReaderId())
                .orElseThrow(() -> new EntityNotFoundException("Reader not found with ID: " + dto.getReaderId())));

        Loan saved = loanRepository.save(loan);
        entityManager.refresh(saved);

        if (dto.getItems() != null) {
            for (CreateLoanItemDto itemDto : dto.getItems()) {
                Copy copy = copyService.findById(itemDto.getCopyId())
                        .orElseThrow(
                                () -> new EntityNotFoundException("Copy not found with ID: " + itemDto.getCopyId()));

                LoanItem item = LoanItem.builder()
                        .id(new LoanItemId(saved.getLoanId(), copy.getCopyId()))
                        .loan(saved)
                        .copy(copy)
                        .dueDate(itemDto.getDueDate())
                        .status(LoanItemStatus.prestado)
                        .build();

                loanItemRepository.save(item);
            }
        }

        return toListDto(saved);
    }

    @Transactional
    public LoanListDto update(UpdateLoanDto dto) {
        Loan loan = loanRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Loan not found with ID: " + dto.getId()));

        loan.setObservation(dto.getObservation());

        loan.setReader(readerService.findById(dto.getReaderId())
                .orElseThrow(() -> new EntityNotFoundException("Reader not found with ID: " + dto.getReaderId())));

        Loan saved = loanRepository.save(loan);

        loanItemRepository.deleteAllByLoan(saved);

        if (dto.getItems() != null) {
            for (UpdateLoanItemDto itemDto : dto.getItems()) {
                Copy copy = copyService.findById(itemDto.getCopyId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Copy not found with ID: " + itemDto.getCopyId()));

                LoanItem item = LoanItem.builder()
                        .id(new LoanItemId(saved.getLoanId(), copy.getCopyId()))
                        .loan(saved)
                        .copy(copy)
                        .dueDate(itemDto.getDueDate())
                        .status(itemDto.getStatus())
                        .build();

                loanItemRepository.save(item);
            }
        }

        return toListDto(saved);
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .books(bookService.getForSelect())
                .students(readerService.getForSelect())
                .build();
    }

    public LoanDto toDto(Loan loan) {
        List<LoanItemDto> items = loanItemRepository.findByLoan(loan).stream()
                .map(item -> {
                    return LoanItemDto.builder()
                            .copy(copyService.toDto(item.getCopy()))
                            .dueDate(item.getDueDate())
                            .returnDate(item.getReturnDate())
                            .status(item.getStatus().name())
                            .build();
                }).toList();

        return LoanDto.builder()
                .id(loan.getLoanId())
                .code(loan.getCode())
                .reader(readerService.toDto(loan.getReader()))
                .loanDate(loan.getLoanDate())
                .observation(loan.getObservation())
                .items(items)
                .build();
    }

    private LoanListDto toListDto(Loan loan) {
        return new LoanListDto(
                loan.getCode(),
                loan.getReader().getCode(),
                loan.getReader().getFullName(),
                loan.getLoanDate(),
                loan.getObservation(),
                loan.getLoanId());
    }
}
