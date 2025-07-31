package com.bookstudio.loan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstudio.loan.model.Loan;
import com.bookstudio.loan.relation.LoanItem;
import com.bookstudio.loan.relation.LoanItemId;

public interface LoanItemRepository extends JpaRepository<LoanItem, LoanItemId> {
    List<LoanItem> findAllByOrderByIdDesc();
    void deleteAllByLoan(Loan loan);
    List<LoanItem> findByLoan(Loan loan);
}
