package com.bookstudio.payment.relation;

import com.bookstudio.fine.model.Fine;
import com.bookstudio.payment.model.Payment;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_fines", schema = "bookstudio_db")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentFine {
    
    @EmbeddedId
    private PaymentFineId id;

    @ManyToOne
    @MapsId("paymentId")
    private Payment payment;

    @ManyToOne
    @MapsId("fineId")
    private Fine fine;
}
