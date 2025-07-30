package com.bookstudio.reservation.service;

import com.bookstudio.reservation.dto.CreateReservationDto;
import com.bookstudio.reservation.dto.ReservationResponseDto;
import com.bookstudio.reservation.dto.UpdateReservationDto;
import com.bookstudio.reservation.model.Reservation;
import com.bookstudio.reservation.projection.ReservationInfoProjection;
import com.bookstudio.reservation.projection.ReservationListProjection;
import com.bookstudio.reservation.repository.ReservationRepository;
import com.bookstudio.reader.service.ReaderService;
import com.bookstudio.copy.service.CopyService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReaderService readerService;
    private final CopyService copyService;

    public List<ReservationListProjection> getList() {
        return reservationRepository.findList();
    }

    public Optional<Reservation> findById(Long reservationId) {
        return reservationRepository.findById(reservationId);
    }

    public Optional<ReservationInfoProjection> getInfoById(Long reservationId) {
        return reservationRepository.findInfoById(reservationId);
    }

    @Transactional
    public ReservationResponseDto create(CreateReservationDto dto) {
        Reservation reservation = Reservation.builder()
                .reader(readerService.findById(dto.getReaderId())
                        .orElseThrow(() -> new RuntimeException("Reader not found with ID: " + dto.getReaderId())))
                .copy(copyService.findById(dto.getCopyId())
                        .orElseThrow(() -> new RuntimeException("Copy not found with ID: " + dto.getCopyId())))
                .reservationDate(dto.getReservationDate())
                .status(dto.getStatus())
                .build();

        Reservation saved = reservationRepository.save(reservation);

        return new ReservationResponseDto(
                saved.getReservationId(),
                saved.getCode(),
                saved.getReader().getFirstName() + " " + saved.getReader().getLastName(),
                saved.getCopy().getCode(),
                saved.getReservationDate(),
                saved.getStatus().name()
        );
    }

    @Transactional
    public ReservationResponseDto update(UpdateReservationDto dto) {
        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + dto.getReservationId()));

        reservation.setReader(readerService.findById(dto.getReaderId())
                .orElseThrow(() -> new RuntimeException("Reader not found with ID: " + dto.getReaderId())));
        reservation.setCopy(copyService.findById(dto.getCopyId())
                .orElseThrow(() -> new RuntimeException("Copy not found with ID: " + dto.getCopyId())));
        reservation.setReservationDate(dto.getReservationDate());
        reservation.setStatus(dto.getStatus());

        Reservation saved = reservationRepository.save(reservation);

        return new ReservationResponseDto(
                saved.getReservationId(),
                saved.getCode(),
                saved.getReader().getFirstName() + " " + saved.getReader().getLastName(),
                saved.getCopy().getCode(),
                saved.getReservationDate(),
                saved.getStatus().name()
        );
    }
}
