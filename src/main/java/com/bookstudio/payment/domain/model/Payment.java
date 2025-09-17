package com.bookstudio.payment.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bookstudio.payment.domain.model.type.PaymentMethod;
import com.bookstudio.reader.domain.model.Reader;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(insertable = false, updatable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "reader_id", nullable = false)
    private Reader reader;

    @Column(nullable = false)
    private BigDecimal amount;

    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;
}