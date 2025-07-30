package com.bookstudio.loan.model;

import java.io.Serializable;
import java.time.LocalDate;

import com.bookstudio.copy.model.Copy;
import com.bookstudio.shared.enums.LoanItemStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "loan_items", schema = "bookstudio_db")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanItem {
    
    @EmbeddedId
    private LoanItemId id;

    @ManyToOne
    @MapsId("loanId")
    private Loan loan;

    @ManyToOne
    @MapsId("copyId")
    private Copy copy;

    private LocalDate dueDate;

    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    private LoanItemStatus status;
}

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanItemId implements Serializable {
    private Long loanId;
    private Long copyId;
}