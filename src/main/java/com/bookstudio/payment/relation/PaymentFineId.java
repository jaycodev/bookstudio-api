package com.bookstudio.payment.relation;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFineId implements Serializable {
    private Long paymentId;
    private Long fineId;
}