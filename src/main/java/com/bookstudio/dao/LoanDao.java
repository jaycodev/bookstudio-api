package com.bookstudio.dao;

import com.bookstudio.models.Loan;

public interface LoanDao extends CrudDao<Loan, String> {
	public int confirmReturn(String loanId);
}
