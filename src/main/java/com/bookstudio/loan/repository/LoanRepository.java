package com.bookstudio.loan.repository;

import com.bookstudio.loan.model.Loan;
import com.bookstudio.loan.projection.LoanInfoProjection;
import com.bookstudio.loan.projection.LoanListProjection;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("""
        SELECT 
            l.id AS loanId,

            b.id AS bookId,
            b.title AS bookTitle,

            s.id AS studentId,
            CONCAT(s.firstName, ' ', s.lastName) AS studentFullName,

            l.loanDate AS loanDate,
            l.returnDate AS returnDate,
            l.quantity AS quantity,
            l.status AS status
        FROM Loan l
        JOIN l.book b
        JOIN l.student s
    """)
    List<LoanListProjection> findList();

    @Query("""
        SELECT 
            l.id AS loanId,

            b.id AS bookId,
            b.title AS bookTitle,

            s.id AS studentId,
            CONCAT(s.firstName, ' ', s.lastName) AS studentFullName,

            l.loanDate AS loanDate,
            l.returnDate AS returnDate,
            l.quantity AS quantity,
            l.observation AS observation,
            l.status AS status
        FROM Loan l
        JOIN l.book b
        JOIN l.student s
        WHERE l.id = :id
    """)
    Optional<LoanInfoProjection> findInfoById(@Param("id") Long id);
}
