package com.bookstudio.loan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstudio.loan.model.LoanItem;
import com.bookstudio.loan.model.LoanItemId;

public interface LoanItemRepository extends JpaRepository<LoanItem, LoanItemId> {
    List<LoanItem> findAllByOrderByIdDesc();
}
