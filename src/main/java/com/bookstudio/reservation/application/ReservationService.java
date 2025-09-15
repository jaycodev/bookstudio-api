package com.bookstudio.reservation.application;

import com.bookstudio.reservation.domain.dto.request.CreateReservationRequest;
import com.bookstudio.reservation.domain.dto.request.UpdateReservationRequest;
import com.bookstudio.reservation.domain.dto.response.ReservationDetailResponse;
import com.bookstudio.reservation.domain.dto.response.ReservationListResponse;
import com.bookstudio.reservation.domain.model.Reservation;
import com.bookstudio.reservation.infrastructure.repository.ReservationRepository;
import com.bookstudio.copy.application.CopyService;
import com.bookstudio.reader.application.ReaderService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ReservationRepository reservationRepository;
    private final ReaderService readerService;
    private final CopyService copyService;

    public List<ReservationListResponse> getList() {
        return reservationRepository.findList();
    }

    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    public ReservationDetailResponse getDetailById(Long id) {
        return reservationRepository.findDetailById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + id));
    }

    @Transactional
    public ReservationListResponse create(CreateReservationRequest request) {
        Reservation reservation = Reservation.builder()
                .reader(readerService.findById(request.getReaderId())
                        .orElseThrow(
                                () -> new EntityNotFoundException(
                                        "Reader not found with ID: " + request.getReaderId())))
                .copy(copyService.findById(request.getCopyId())
                        .orElseThrow(
                                () -> new EntityNotFoundException("Copy not found with ID: " + request.getCopyId())))
                .reservationDate(request.getReservationDate())
                .status(request.getStatus())
                .build();

        Reservation saved = reservationRepository.save(reservation);
        entityManager.refresh(saved);

        return toListResponse(saved);
    }

    @Transactional
    public ReservationListResponse update(Long id, UpdateReservationRequest request) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with ID: " + id));

        reservation.setReader(readerService.findById(request.getReaderId())
                .orElseThrow(() -> new EntityNotFoundException("Reader not found with ID: " + request.getReaderId())));
        reservation.setCopy(copyService.findById(request.getCopyId())
                .orElseThrow(() -> new EntityNotFoundException("Copy not found with ID: " + request.getCopyId())));

        reservation.setReservationDate(request.getReservationDate());
        reservation.setStatus(request.getStatus());

        Reservation updated = reservationRepository.save(reservation);
        return toListResponse(updated);
    }

    private ReservationListResponse toListResponse(Reservation reservation) {
        return new ReservationListResponse(
                reservation.getId(),
                reservation.getCode(),
                new ReservationListResponse.Reader(
                        reservation.getReader().getId(),
                        reservation.getReader().getCode(),
                        reservation.getReader().getFullName()),
                new ReservationListResponse.Copy(
                        reservation.getCopy().getCode()),
                reservation.getReservationDate(),
                reservation.getStatus());
    }
}
