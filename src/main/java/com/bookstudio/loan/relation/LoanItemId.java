package com.bookstudio.loan.relation;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanItemId implements Serializable {

    @Column(name = "loan_id")
    private Long loanId;

    @Column(name = "copy_id")
    private Long copyId;
}
