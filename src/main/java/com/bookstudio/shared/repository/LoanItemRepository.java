package com.bookstudio.shared.repository;

import com.bookstudio.shared.model.LoanItem;
import com.bookstudio.shared.model.LoanItemId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanItemRepository extends JpaRepository<LoanItem, LoanItemId> {
    List<LoanItem> findAllByOrderByIdDesc();
}
