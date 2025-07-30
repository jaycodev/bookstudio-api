package com.bookstudio.reservation.projection;

import java.time.LocalDate;

public interface ReservationInfoProjection {
    Long getReservationId();
    String getCode();
    String getReaderName();
    String getCopyCode();
    LocalDate getReservationDate();
    String getStatus();
}
