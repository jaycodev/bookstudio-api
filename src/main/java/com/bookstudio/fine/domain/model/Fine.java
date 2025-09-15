package com.bookstudio.fine.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bookstudio.loan.domain.model.LoanItem;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(insertable = false, updatable = false)
    private String code;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "loan_id", referencedColumnName = "loan_id"),
            @JoinColumn(name = "copy_id", referencedColumnName = "copy_id")
    })
    private LoanItem loanItem;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Integer daysLate;

    @Enumerated(EnumType.STRING)
    private FineStatus status;

    private LocalDate issuedAt;
}
