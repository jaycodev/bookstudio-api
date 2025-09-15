package com.bookstudio.loan.domain.model;

import java.time.LocalDate;

import com.bookstudio.copy.domain.model.Copy;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "loan_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanItem {
    
    @EmbeddedId
    private LoanItemId id;

    @ManyToOne
    @MapsId("loanId")
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @ManyToOne
    @MapsId("copyId")
    @JoinColumn(name = "copy_id")
    private Copy copy;

    private LocalDate dueDate;

    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    private LoanItemStatus status;
}
