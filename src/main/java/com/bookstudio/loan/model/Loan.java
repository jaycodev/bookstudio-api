package com.bookstudio.loan.model;

import com.bookstudio.book.model.Book;
import com.bookstudio.shared.enums.LoanStatus;
import com.bookstudio.shared.util.IdFormatter;
import com.bookstudio.student.model.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LoanID")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BookID", nullable = false)
	private Book book;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "StudentID", nullable = false)
	private Student student;

	@Column(name = "LoanDate", nullable = false)
	private LocalDate loanDate;

	@Column(name = "ReturnDate", nullable = false)
	private LocalDate returnDate;

	@Column(name = "Quantity", nullable = false)
	private int quantity;

    @Enumerated(EnumType.STRING)
	@Column(name = "Status", columnDefinition = "ENUM('prestado','devuelto')")
	private LoanStatus status;

	@Column(name = "Observation", columnDefinition = "TEXT")
	private String observation;

	@Transient
	public String getFormattedId() {
		return IdFormatter.formatId(String.valueOf(id), "P");
	}
}
