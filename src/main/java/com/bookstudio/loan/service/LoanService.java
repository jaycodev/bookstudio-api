package com.bookstudio.loan.service;

import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.bookstudio.book.dao.BookDao;
import com.bookstudio.book.dao.BookDaoImpl;
import com.bookstudio.loan.dao.LoanDao;
import com.bookstudio.loan.dao.LoanDaoImpl;
import com.bookstudio.loan.model.Loan;
import com.bookstudio.shared.util.SelectOptions;
import com.bookstudio.student.dao.StudentDao;
import com.bookstudio.student.dao.StudentDaoImpl;

public class LoanService {
	private LoanDao loanDao = new LoanDaoImpl();
	private BookDao bookDao = new BookDaoImpl();
	private StudentDao studentDao = new StudentDaoImpl();

	public List<Loan> listLoans() throws Exception {
		return loanDao.listAll();
	}

	public Loan getLoan(String loanId) throws Exception {
		return loanDao.getById(loanId);
	}

	public Loan createLoan(HttpServletRequest request) throws Exception {
		String bookId = request.getParameter("addLoanBook");
		String studentId = request.getParameter("addLoanStudent");
		LocalDate loanDate = LocalDate.now();
		LocalDate returnDate = LocalDate.parse(request.getParameter("addReturnDate"));
		int quantity = Integer.parseInt(request.getParameter("addLoanQuantity"));
		String status = "prestado";
		String observation = request.getParameter("addLoanObservation");

		Loan loan = new Loan();
		loan.setBookId(bookId);
		loan.setStudentId(studentId);
		loan.setLoanDate(loanDate);
		loan.setReturnDate(returnDate);
		loan.setQuantity(quantity);
		loan.setStatus(status);
		loan.setObservation(observation);

		return loanDao.create(loan);
	}

	public Loan updateLoan(String loanId, HttpServletRequest request) throws Exception {
		String bookId = request.getParameter("editLoanBook");
		String studentId = request.getParameter("editLoanStudent");
		LocalDate returnDate = LocalDate.parse(request.getParameter("editReturnDate"));
		String observation = request.getParameter("editLoanObservation");

		Loan loan = new Loan();
		loan.setLoanId(loanId);
		loan.setBookId(bookId);
		loan.setStudentId(studentId);
		loan.setReturnDate(returnDate);
		loan.setObservation(observation);

		return loanDao.update(loan);
	}

	public int confirmReturn(String loanId) throws Exception {
		return loanDao.confirmReturn(loanId);
	}

	public SelectOptions populateSelects() throws Exception {
		SelectOptions selectOptions = new SelectOptions();

		selectOptions.setBooks(bookDao.populateBookSelect());

		selectOptions.setStudents(studentDao.populateStudentSelect());

		return selectOptions;
	}
}
