package com.bookstudio.reservation.domain.model;

import java.time.LocalDate;

import com.bookstudio.copy.domain.model.Copy;
import com.bookstudio.reader.domain.model.Reader;
import com.bookstudio.reservation.domain.model.type.ReservationStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(insertable = false, updatable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "reader_id", nullable = false)
    private Reader reader;

    @ManyToOne
    @JoinColumn(name = "copy_id", nullable = false)
    private Copy copy;

    private LocalDate reservationDate;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
}
