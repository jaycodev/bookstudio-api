package com.bookstudio.payment.repository;

import com.bookstudio.payment.model.Payment;
import com.bookstudio.payment.projection.PaymentListProjection;
import com.bookstudio.payment.projection.PaymentInfoProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("""
        SELECT
            p.paymentId AS paymentId,
            p.code AS code,
            CONCAT(r.firstName, ' ', r.lastName) AS readerFullName,
            p.amount AS amount,
            p.paymentDate AS paymentDate,
            p.method AS method
        FROM Payment p
        JOIN p.reader r
        ORDER BY p.paymentId DESC
    """)
    List<PaymentListProjection> findList();

    @Query("""
        SELECT
            p.paymentId AS paymentId,
            p.code AS code,
            CONCAT(r.firstName, ' ', r.lastName) AS readerFullName,
            p.amount AS amount,
            p.paymentDate AS paymentDate,
            p.method AS method
        FROM Payment p
        JOIN p.reader r
        WHERE p.paymentId = :id
    """)
    Optional<PaymentInfoProjection> findInfoById(@Param("id") Long id);
}
