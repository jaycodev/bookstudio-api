package com.bookstudio.reservation.service;

import com.bookstudio.reservation.dto.CreateReservationDto;
import com.bookstudio.reservation.dto.ReservationDetailDto;
import com.bookstudio.reservation.dto.ReservationListDto;
import com.bookstudio.reservation.dto.UpdateReservationDto;
import com.bookstudio.reservation.model.Reservation;
import com.bookstudio.reservation.repository.ReservationRepository;
import com.bookstudio.reader.service.ReaderService;
import com.bookstudio.copy.service.CopyService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ReservationRepository reservationRepository;
    private final ReaderService readerService;
    private final CopyService copyService;

    public List<ReservationListDto> getList() {
        return reservationRepository.findList();
    }

    public Optional<Reservation> findById(Long reservationId) {
        return reservationRepository.findById(reservationId);
    }

    public ReservationDetailDto getInfoById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with ID: " + reservationId));

        return ReservationDetailDto.builder()
                .id(reservation.getReservationId())
                .code(reservation.getCode())
                .reader(readerService.toSummaryDto(reservation.getReader()))
                .copy(copyService.toSummaryDto(reservation.getCopy()))
                .reservationDate(reservation.getReservationDate())
                .status(reservation.getStatus().name())
                .build();
    }

    @Transactional
    public ReservationListDto create(CreateReservationDto dto) {
        Reservation reservation = Reservation.builder()
                .reader(readerService.findById(dto.getReaderId())
                        .orElseThrow(
                                () -> new EntityNotFoundException("Reader not found with ID: " + dto.getReaderId())))
                .copy(copyService.findById(dto.getCopyId())
                        .orElseThrow(() -> new EntityNotFoundException("Copy not found with ID: " + dto.getCopyId())))
                .reservationDate(dto.getReservationDate())
                .status(dto.getStatus())
                .build();

        Reservation saved = reservationRepository.save(reservation);
        entityManager.refresh(saved);

        return toListDto(saved);
    }

    @Transactional
    public ReservationListDto update(UpdateReservationDto dto) {
        Reservation reservation = reservationRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with ID: " + dto.getId()));

        reservation.setReader(readerService.findById(dto.getReaderId())
                .orElseThrow(() -> new EntityNotFoundException("Reader not found with ID: " + dto.getReaderId())));
        reservation.setCopy(copyService.findById(dto.getCopyId())
                .orElseThrow(() -> new EntityNotFoundException("Copy not found with ID: " + dto.getCopyId())));

        reservation.setReservationDate(dto.getReservationDate());
        reservation.setStatus(dto.getStatus());

        Reservation saved = reservationRepository.save(reservation);
        return toListDto(saved);
    }

    private ReservationListDto toListDto(Reservation reservation) {
        return new ReservationListDto(
                reservation.getCode(),
                reservation.getReader().getCode(),
                reservation.getReader().getFullName(),
                reservation.getCopy().getCode(),
                reservation.getReservationDate(),
                reservation.getStatus(),
                reservation.getReservationId());
    }
}
