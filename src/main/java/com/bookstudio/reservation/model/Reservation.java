package com.bookstudio.reservation.model;

import java.time.LocalDate;

import com.bookstudio.copy.model.Copy;
import com.bookstudio.reader.model.Reader;
import com.bookstudio.shared.enums.ReservationStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservations", schema = "bookstudio_db")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

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
