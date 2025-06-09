package com.bookstudio.loan.dao;

import com.bookstudio.loan.model.Loan;
import com.bookstudio.shared.dao.CrudDao;

public interface LoanDao extends CrudDao<Loan, String> {
	public int confirmReturn(String loanId);
}
