package com.bookstudio.payment.repository;

import com.bookstudio.payment.dto.PaymentListDto;
import com.bookstudio.payment.model.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("""
        SELECT new com.bookstudio.payment.dto.PaymentListDto(
            p.code,
            CONCAT(r.firstName, ' ', r.lastName),
            p.amount,
            p.paymentDate,
            p.method,
            p.paymentId 
        )
        FROM Payment p
        JOIN p.reader r
        ORDER BY p.paymentId DESC
    """)
    List<PaymentListDto> findList();
}
