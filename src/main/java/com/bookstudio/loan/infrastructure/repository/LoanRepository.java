package com.bookstudio.loan.infrastructure.repository;

import com.bookstudio.loan.application.dto.response.LoanDetailResponse;
import com.bookstudio.loan.application.dto.response.LoanListResponse;
import com.bookstudio.loan.domain.model.Loan;
import com.bookstudio.shared.application.dto.response.OptionResponse;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("""
        SELECT new com.bookstudio.loan.application.dto.response.LoanListResponse(
            l.id,
            l.code,
            new com.bookstudio.loan.application.dto.response.LoanListResponse$Reader(
                r.id,
                r.code,
                CONCAT(r.firstName, ' ', r.lastName)
            ),
            l.loanDate,
            new com.bookstudio.loan.application.dto.response.LoanListResponse$ItemCounts(
                COALESCE(SUM(CASE WHEN li.status = com.bookstudio.loan.domain.model.type.LoanItemStatus.PRESTADO THEN 1 ELSE 0 END), 0),
                COALESCE(SUM(CASE WHEN li.status = com.bookstudio.loan.domain.model.type.LoanItemStatus.DEVUELTO THEN 1 ELSE 0 END), 0),
                COALESCE(SUM(CASE WHEN li.status = com.bookstudio.loan.domain.model.type.LoanItemStatus.RETRASADO THEN 1 ELSE 0 END), 0),
                COALESCE(SUM(CASE WHEN li.status = com.bookstudio.loan.domain.model.type.LoanItemStatus.EXTRAVIADO THEN 1 ELSE 0 END), 0),
                COALESCE(SUM(CASE WHEN li.status = com.bookstudio.loan.domain.model.type.LoanItemStatus.CANCELADO THEN 1 ELSE 0 END), 0)
            )
        )
        FROM Loan l
        JOIN l.reader r
        LEFT JOIN l.loanItems li
        GROUP BY l.id, l.code, r.id, r.code, r.firstName, r.lastName, l.loanDate
        ORDER BY l.id DESC
    """)
    List<LoanListResponse> findList();

    @Query("""
        SELECT
            l.id AS value,
            l.code AS label
        FROM Loan l
        ORDER BY l.code DESC
    """)
    List<OptionResponse> findForOptions();

    @Query("""
        SELECT new com.bookstudio.loan.application.dto.response.LoanDetailResponse(
            l.id,
            l.code,
            new com.bookstudio.loan.application.dto.response.LoanDetailResponse$Reader(
                r.id,
                r.code,
                CONCAT(r.firstName, ' ', r.lastName)
            ),
            l.loanDate,
            l.observation,
            NULL
        )
        FROM Loan l
        JOIN l.reader r
        WHERE l.id = :id
    """)
    Optional<LoanDetailResponse> findDetailById(Long id);

    @Query("""
        SELECT new com.bookstudio.loan.application.dto.response.LoanListResponse$ItemCounts(
            COALESCE(SUM(CASE WHEN li.status = com.bookstudio.loan.domain.model.type.LoanItemStatus.PRESTADO THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN li.status = com.bookstudio.loan.domain.model.type.LoanItemStatus.DEVUELTO THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN li.status = com.bookstudio.loan.domain.model.type.LoanItemStatus.RETRASADO THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN li.status = com.bookstudio.loan.domain.model.type.LoanItemStatus.EXTRAVIADO THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN li.status = com.bookstudio.loan.domain.model.type.LoanItemStatus.CANCELADO THEN 1 ELSE 0 END), 0)
        )
        FROM LoanItem li
        WHERE li.loan.id = :id
    """)
    LoanListResponse.ItemCounts findItemCountsById(Long id);
}
