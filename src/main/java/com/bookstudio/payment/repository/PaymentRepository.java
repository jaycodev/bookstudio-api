package com.bookstudio.payment.repository;

import com.bookstudio.payment.dto.PaymentListDto;
import com.bookstudio.payment.model.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("""
        SELECT new com.bookstudio.payment.dto.PaymentListDto(
            p.paymentId,
            p.code,
            COUNT(f),
            r.readerId,
            r.code,
            CONCAT(r.firstName, ' ', r.lastName),
            p.amount,
            p.paymentDate,
            p.method
        )
        FROM Payment p
        JOIN p.reader r
        JOIN PaymentFine pf ON pf.payment = p
        JOIN Fine f ON pf.fine = f
        GROUP BY p.code, r.readerId, r.code, r.firstName, r.lastName, p.amount, p.paymentDate, p.method, p.paymentId
        ORDER BY p.paymentId DESC
    """)
    List<PaymentListDto> findList();
}
