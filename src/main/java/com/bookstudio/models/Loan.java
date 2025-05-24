package com.bookstudio.models;

import java.time.LocalDate;

import com.bookstudio.utils.IdFormatter;

public class Loan {
	private String loanId;
	private String formattedLoanId;
	private String bookId;
	private String formattedBookId;
	private String bookTitle;
	private String studentId;
	private String formattedStudentId;
	private String studentFullName;
	private LocalDate loanDate;
	private LocalDate returnDate;
	private int quantity;
	private String status;
	private String observation;

	public String getLoanId() {
		return loanId;
	}
	
	public void setLoanId(String loanId) {
		this.loanId = loanId;
        this.formattedLoanId = IdFormatter.formatId(loanId, "P");
	}
	
	public String getFormattedLoanId() {
		return formattedLoanId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
        this.formattedBookId = IdFormatter.formatId(bookId, "L");
	}
	
	public String getFormattedBookId() {
		return formattedBookId;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
        this.formattedStudentId = IdFormatter.formatId(studentId, "ES");
	}
	
	public String getFormattedStudentId() {
		return formattedStudentId;
	}

	public String getStudentFullName() {
		return studentFullName;
	}

	public void setStudentFullName(String studentFullName) {
		this.studentFullName = studentFullName;
	}

	public LocalDate getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(LocalDate loanDate) {
		this.loanDate = loanDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}
}
