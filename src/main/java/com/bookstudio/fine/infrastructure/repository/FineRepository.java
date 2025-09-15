package com.bookstudio.fine.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstudio.fine.domain.dto.response.FineDetailResponse;
import com.bookstudio.fine.domain.dto.response.FineListResponse;
import com.bookstudio.fine.domain.model.Fine;
import com.bookstudio.shared.domain.dto.response.OptionResponse;

public interface FineRepository extends JpaRepository<Fine, Long> {
    @Query("""
        SELECT new com.bookstudio.fine.domain.dto.response.FineListResponse(
            f.id,
            f.code,
            new com.bookstudio.fine.domain.dto.response.FineListResponse$Loan(
                l.id,
                l.code
            ),
            new com.bookstudio.fine.domain.dto.response.FineListResponse$Copy(
                c.id,
                c.code
            ),
            f.amount,
            f.daysLate,
            f.issuedAt,
            f.status
        )
        FROM Fine f
        JOIN f.loanItem li
        JOIN li.loan l
        JOIN li.copy c
        ORDER BY f.id DESC
    """)
    List<FineListResponse> findList();

    @Query("""
        SELECT
            f.id AS value,
            f.code AS label
        FROM Fine f
        WHERE f.status = com.bookstudio.fine.domain.model.FineStatus.PENDIENTE
    """)
    List<OptionResponse> findForOptions();

    @Query("""
        SELECT new com.bookstudio.fine.domain.dto.response.FineDetailResponse(
            f.id,
            f.code,
            new com.bookstudio.fine.domain.dto.response.FineDetailResponse$LoanItem(
                new com.bookstudio.fine.domain.dto.response.FineDetailResponse$LoanItem$Copy(
                    c.id,
                    c.code,
                    c.barcode,
                    c.status
                ),
                li.dueDate,
                li.returnDate,
                li.status
            ),
            f.amount,
            f.daysLate,
            f.status,
            f.issuedAt
        )
        FROM Fine f
        JOIN f.loanItem li
        JOIN li.copy c
        WHERE f.id = :id
    """)
    Optional<FineDetailResponse> findDetailById(Long id);
}
