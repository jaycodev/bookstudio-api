package com.bookstudio.payment.domain.model;

import com.bookstudio.fine.domain.model.Fine;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_fines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentFine {
    
    @EmbeddedId
    private PaymentFineId id;

    @ManyToOne
    @MapsId("paymentId")
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @MapsId("fineId")
    @JoinColumn(name = "fine_id")
    private Fine fine;
}
