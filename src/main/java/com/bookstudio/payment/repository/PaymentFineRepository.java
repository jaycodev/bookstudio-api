package com.bookstudio.payment.repository;

import com.bookstudio.fine.dto.FineSummaryDto;
import com.bookstudio.payment.model.Payment;
import com.bookstudio.payment.relation.PaymentFine;
import com.bookstudio.payment.relation.PaymentFineId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentFineRepository extends JpaRepository<PaymentFine, PaymentFineId> {
    Long countByPayment(Payment payment);

    @Query("""
        SELECT new com.bookstudio.fine.dto.FineSummaryDto(
            f.fineId, f.code, f.amount, f.status
        )
        FROM PaymentFine pf
        JOIN pf.fine f
        WHERE pf.payment.paymentId = :paymentId
    """)
    List<FineSummaryDto> findFineSummariesByPaymentId(@Param("paymentId") Long paymentId);

    void deleteAllByPayment(Payment payment);
}