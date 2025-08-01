package com.bookstudio.payment.repository;

import com.bookstudio.fine.dto.FineFlatDto;
import com.bookstudio.payment.model.Payment;
import com.bookstudio.payment.relation.PaymentFine;
import com.bookstudio.payment.relation.PaymentFineId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentFineRepository extends JpaRepository<PaymentFine, PaymentFineId> {
    @Query("""
        SELECT new com.bookstudio.fine.dto.FineFlatDto(
            f.fineId, f.code,
            li.id.loanId, li.id.copyId,
            li.dueDate, li.returnDate, li.status,
            c.code,
            f.amount, f.daysLate, f.status, f.issuedAt
        )
        FROM PaymentFine pf
        JOIN pf.fine f
        JOIN f.loanItem li
        JOIN li.copy c
        WHERE pf.payment.paymentId = :paymentId
    """)
    List<FineFlatDto> findFineFlatDtosByPaymentId(@Param("paymentId") Long paymentId);

    void deleteAllByPayment(Payment payment);
}