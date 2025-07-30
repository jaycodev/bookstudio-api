package com.bookstudio.fine.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstudio.fine.model.Fine;
import com.bookstudio.fine.projection.FineInfoProjection;
import com.bookstudio.fine.projection.FineListProjection;
import com.bookstudio.fine.projection.FineSelectProjection;

public interface FineRepository extends JpaRepository<Fine, Long> {
    @Query("""
        SELECT
            f.fineId AS fineId,
            f.code AS code,
            l.code AS loanCode,
            f.amount AS amount,
            f.daysLate AS daysLate,
            f.status AS status,
            f.issuedAt AS issuedAt
        FROM Fine f
        JOIN f.loanItem li
        JOIN li.loan l
        ORDER BY f.fineId DESC
    """)
    List<FineListProjection> findList();

    @Query("""
        SELECT
            f.fineId AS fineId,
            f.code AS code,
            l.code AS loanCode,
            f.amount AS amount,
            f.daysLate AS daysLate,
            f.status AS status,
            f.issuedAt AS issuedAt
        FROM Fine f
        JOIN f.loanItem li
        JOIN li.loan l
        WHERE f.fineId = :id
    """)
    Optional<FineInfoProjection> findInfoById(@Param("id") Long id);

    @Query("""
        SELECT
            f.fineId AS fineId,
            f.code AS code
        FROM Fine f
        WHERE f.status = 'pendiente'
    """)
    List<FineSelectProjection> findForSelect();
}
