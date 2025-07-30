package com.bookstudio.payment.relation;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFineId implements Serializable {

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "fine_id")
    private Long fineId;
}