package com.bookstudio.loan.service;

import com.bookstudio.book.model.Book;
import com.bookstudio.book.repository.BookRepository;
import com.bookstudio.book.service.BookService;
import com.bookstudio.loan.dto.CreateLoanDto;
import com.bookstudio.loan.dto.LoanResponseDto;
import com.bookstudio.loan.dto.UpdateLoanDto;
import com.bookstudio.loan.model.Loan;
import com.bookstudio.loan.projection.LoanInfoProjection;
import com.bookstudio.loan.projection.LoanListProjection;
import com.bookstudio.loan.repository.LoanRepository;
import com.bookstudio.shared.enums.LoanStatus;
import com.bookstudio.shared.util.SelectOptions;
import com.bookstudio.student.model.Student;
import com.bookstudio.student.repository.StudentRepository;
import com.bookstudio.student.service.StudentService;

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
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;

    private final BookService bookService;
    private final StudentService studentService;

    public List<LoanListProjection> getList() {
        return loanRepository.findList();
    }

    public Optional<LoanInfoProjection> getInfoById(Long loanId) {
        return loanRepository.findInfoById(loanId);
    }

    @Transactional
    public LoanResponseDto create(CreateLoanDto dto) {
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        book.setLoanedCopies(0);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setStudent(student);
        loan.setLoanDate(LocalDate.now());
        loan.setReturnDate(dto.getReturnDate());
        loan.setQuantity(dto.getQuantity());
        loan.setObservation(dto.getObservation());
        loan.setStatus(LoanStatus.prestado);

        Loan saved = loanRepository.save(loan);

        return new LoanResponseDto(
                saved.getId(),
                String.valueOf(saved.getBook().getId()),
                saved.getBook().getTitle(),
                String.valueOf(saved.getStudent().getId()),
                saved.getStudent().getFullName(),
                saved.getLoanDate(),
                saved.getReturnDate(),
                saved.getQuantity(),
                saved.getStatus().name());
    }

    @Transactional
    public LoanResponseDto update(UpdateLoanDto dto) {
        Loan loan = loanRepository.findById(dto.getLoanId())
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + dto.getLoanId()));

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        loan.setStudent(student);
        loan.setReturnDate(dto.getReturnDate());
        loan.setObservation(dto.getObservation());

        Loan saved = loanRepository.save(loan);

        return new LoanResponseDto(
                saved.getId(),
                String.valueOf(saved.getBook().getId()),
                saved.getBook().getTitle(),
                String.valueOf(saved.getStudent().getId()),
                saved.getStudent().getFullName(),
                saved.getLoanDate(),
                saved.getReturnDate(),
                saved.getQuantity(),
                saved.getStatus().name());
    }

    @Transactional
    public int markAsReturned(Long loanId) {
        Optional<Loan> optionalLoan = loanRepository.findById(loanId);

        if (optionalLoan.isEmpty())
            return 0;

        Loan loan = optionalLoan.get();
        if (loan.getStatus() == LoanStatus.devuelto) {
            return 0;
        }

        Book book = loan.getBook();
        book.setLoanedCopies(book.getLoanedCopies() - loan.getQuantity());
        bookRepository.save(book);

        loan.setStatus(LoanStatus.devuelto);
        loanRepository.save(loan);

        return 1;
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .books(bookService.getForSelect())
                .students(studentService.getForSelect())
                .build();
    }
}
