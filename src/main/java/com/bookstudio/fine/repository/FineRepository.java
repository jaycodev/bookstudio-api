package com.bookstudio.fine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstudio.fine.dto.FineListDto;
import com.bookstudio.fine.dto.FineOptionDto;
import com.bookstudio.fine.model.Fine;

public interface FineRepository extends JpaRepository<Fine, Long> {
    @Query("""
        SELECT new com.bookstudio.fine.dto.FineListDto(
            f.fineId,
            f.code,
            l.loanId,
            l.code,
            c.copyId,
            c.code,
            f.amount,
            f.daysLate,
            f.issuedAt,
            f.status
        )
        FROM Fine f
        JOIN f.loanItem li
        JOIN li.loan l
        JOIN li.copy c
        ORDER BY f.fineId DESC
    """)
    List<FineListDto> findList();

    @Query("""
        SELECT new com.bookstudio.fine.dto.FineOptionDto(
            f.fineId,
            f.code
        )
        FROM Fine f
        WHERE f.status = com.bookstudio.fine.model.FineStatus.PENDIENTE
    """)
    List<FineOptionDto> findForOptions();
}
