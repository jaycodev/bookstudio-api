package com.bookstudio.loan.service;

import com.bookstudio.book.model.Book;
import com.bookstudio.book.repository.BookRepository;
import com.bookstudio.book.service.BookService;
import com.bookstudio.loan.model.Loan;
import com.bookstudio.loan.repository.LoanRepository;
import com.bookstudio.shared.enums.LoanStatus;
import com.bookstudio.shared.util.SelectOptions;
import com.bookstudio.student.model.Student;
import com.bookstudio.student.repository.StudentRepository;
import com.bookstudio.student.service.StudentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

	public List<Loan> listLoans() {
		return loanRepository.findAll();
	}

	public Optional<Loan> getLoan(Long loanId) {
		return loanRepository.findById(loanId);
	}

	@Transactional
	public Loan createLoan(Loan loan) {
		Book book = bookRepository.findById(loan.getBook().getId())
				.orElseThrow(() -> new RuntimeException("Libro no encontrado"));
		Student student = studentRepository.findById(loan.getStudent().getId())
				.orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

		book.setLoanedCopies(book.getLoanedCopies() + loan.getQuantity());
		bookRepository.save(book);

		loan.setBook(book);
		loan.setStudent(student);
		return loanRepository.save(loan);
	}

	@Transactional
	public Loan updateLoan(Long loanId, Loan updatedData) {
		return loanRepository.findById(loanId).map(loan -> {
			Student student = studentRepository.findById(updatedData.getStudent().getId())
					.orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

			loan.setStudent(student);
			loan.setReturnDate(updatedData.getReturnDate());
			loan.setObservation(updatedData.getObservation());
			return loanRepository.save(loan);
		}).orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + loanId));
	}

	@Transactional
	public int confirmReturn(Long loanId) {
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

	public SelectOptions populateSelects() {
		return SelectOptions.builder()
				.books(bookService.getBooksForSelect())
				.students(studentService.getStudentsForSelect())
				.build();
	}
}
