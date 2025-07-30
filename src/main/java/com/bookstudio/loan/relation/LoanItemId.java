package com.bookstudio.loan.relation;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanItemId implements Serializable {
    private Long loanId;
    private Long copyId;
}
