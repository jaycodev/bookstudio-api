package com.bookstudio.reservation.presentation;

import com.bookstudio.reservation.application.ReservationService;
import com.bookstudio.reservation.application.dto.request.CreateReservationRequest;
import com.bookstudio.reservation.application.dto.request.UpdateReservationRequest;
import com.bookstudio.reservation.application.dto.response.ReservationDetailResponse;
import com.bookstudio.reservation.application.dto.response.ReservationListResponse;
import com.bookstudio.shared.application.dto.response.ApiErrorResponse;
import com.bookstudio.shared.application.dto.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Operations related to reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    @Operation(summary = "List all reservations")
    public ResponseEntity<?> list() {
        List<ReservationListResponse> reservations = reservationService.getList();
        if (reservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiErrorResponse(false, "No reservations found.", "no_content", 204));
        }
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a reservation by ID")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            ReservationDetailResponse reservation = reservationService.getDetailById(id);
            return ResponseEntity.ok(reservation);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, "Reservation not found.", "not_found", 404));
        }
    }

    @PostMapping
    @Operation(summary = "Create a new reservation")
    public ResponseEntity<?> create(@RequestBody CreateReservationRequest request) {
        try {
            ReservationListResponse created = reservationService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponse(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a reservation by ID")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateReservationRequest request) {
        try {
            ReservationListResponse updated = reservationService.update(id, request);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, e.getMessage(), "update_failed", 404));
        }
    }
}
