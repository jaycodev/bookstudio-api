package com.bookstudio.fine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstudio.fine.dto.FineListDto;
import com.bookstudio.fine.dto.FineSelectDto;
import com.bookstudio.fine.model.Fine;

public interface FineRepository extends JpaRepository<Fine, Long> {
    @Query("""
        SELECT new com.bookstudio.fine.dto.FineListDto(
            f.code,
            l.code,
            f.amount,
            f.daysLate,
            f.status,
            f.issuedAt,
            f.fineId
        )
        FROM Fine f
        JOIN f.loanItem li
        JOIN li.loan l
        ORDER BY f.fineId DESC
    """)
    List<FineListDto> findList();

    @Query("""
        SELECT new com.bookstudio.fine.dto.FineSelectDto(
            f.fineId,
            f.code
        )
        FROM Fine f
        WHERE f.status = com.bookstudio.shared.enums.FineStatus.pendiente
    """)
    List<FineSelectDto> findForSelect();
}
