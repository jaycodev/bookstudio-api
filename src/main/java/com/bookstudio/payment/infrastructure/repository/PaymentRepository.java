package com.bookstudio.payment.infrastructure.repository;

import com.bookstudio.payment.application.dto.response.PaymentDetailResponse;
import com.bookstudio.payment.application.dto.response.PaymentListResponse;
import com.bookstudio.payment.domain.model.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("""
        SELECT new com.bookstudio.payment.application.dto.response.PaymentListResponse(
            p.id,
            p.code,
            COUNT(f),
            new com.bookstudio.payment.application.dto.response.PaymentListResponse$Reader(
                r.id,
                r.code,
                CONCAT(r.firstName, ' ', r.lastName)
            ),
            p.amount,
            p.paymentDate,
            p.method
        )
        FROM Payment p
        JOIN p.reader r
        JOIN PaymentFine pf ON pf.payment = p
        JOIN pf.fine f
        GROUP BY p.id, p.code, r.id, r.code, r.firstName, r.lastName, p.amount, p.paymentDate, p.method
        ORDER BY p.id DESC
    """)
    List<PaymentListResponse> findList();

    @Query("""
        SELECT new com.bookstudio.payment.application.dto.response.PaymentDetailResponse(
            p.id,
            p.code,
            new com.bookstudio.payment.application.dto.response.PaymentDetailResponse$Reader(
                r.id,
                r.code,
                CONCAT(r.firstName, ' ', r.lastName)
            ),
            p.amount,
            p.paymentDate,
            p.method,
            NULL
        )
        FROM Payment p
        JOIN p.reader r
        WHERE p.id = :id
    """)
    Optional<PaymentDetailResponse> findDetailById(Long id);
}
