package com.bookstudio.loan.service;

import com.bookstudio.book.service.BookService;
import com.bookstudio.loan.dto.CreateLoanDto;
import com.bookstudio.loan.dto.LoanResponseDto;
import com.bookstudio.loan.dto.UpdateLoanDto;
import com.bookstudio.loan.model.Loan;
import com.bookstudio.loan.projection.LoanInfoProjection;
import com.bookstudio.loan.projection.LoanListProjection;
import com.bookstudio.loan.repository.LoanRepository;
import com.bookstudio.reader.model.Reader;
import com.bookstudio.reader.repository.ReaderRepository;
import com.bookstudio.reader.service.ReaderService;
import com.bookstudio.shared.util.SelectOptions;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final ReaderRepository readerRepository;

    private final BookService bookService;
    private final ReaderService readerService;

    public List<LoanListProjection> getList() {
        return loanRepository.findList();
    }

    public Optional<LoanInfoProjection> getInfoById(Long loanId) {
        return loanRepository.findInfoById(loanId);
    }

    @Transactional
    public LoanResponseDto create(CreateLoanDto dto) {
        Reader reader = readerRepository.findById(dto.getReaderId())
                .orElseThrow(() -> new RuntimeException("Lector no encontrado"));

        Loan loan = new Loan();
        loan.setReader(reader);
        loan.setLoanDate(LocalDate.now());
        loan.setObservation(dto.getObservation());

        Loan saved = loanRepository.save(loan);

        return new LoanResponseDto(
                saved.getLoanId(),
                saved.getCode(),
                String.valueOf(saved.getReader().getReaderId()),
                saved.getReader().getCode(),
                saved.getReader().getFullName(),
                saved.getLoanDate());
    }

    @Transactional
    public LoanResponseDto update(UpdateLoanDto dto) {
        Loan loan = loanRepository.findById(dto.getLoanId())
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + dto.getLoanId()));

        Reader reader = readerRepository.findById(dto.getReaderId())
                .orElseThrow(() -> new RuntimeException("Lector no encontrado"));

        loan.setReader(reader);
        loan.setObservation(dto.getObservation());

        Loan saved = loanRepository.save(loan);

        return new LoanResponseDto(
                saved.getLoanId(),
                saved.getCode(),
                String.valueOf(saved.getReader().getReaderId()),
                saved.getReader().getCode(),
                saved.getReader().getFullName(),
                saved.getLoanDate());
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .books(bookService.getForSelect())
                .students(readerService.getForSelect())
                .build();
    }
}
