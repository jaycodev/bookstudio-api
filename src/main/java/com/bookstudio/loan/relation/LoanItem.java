package com.bookstudio.loan.relation;

import java.time.LocalDate;

import com.bookstudio.copy.model.Copy;
import com.bookstudio.loan.model.Loan;
import com.bookstudio.shared.enums.LoanItemStatus;

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
