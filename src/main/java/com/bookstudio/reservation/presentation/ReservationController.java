package com.bookstudio.reservation.presentation;

import com.bookstudio.reservation.application.ReservationService;
import com.bookstudio.reservation.application.dto.request.CreateReservationRequest;
import com.bookstudio.reservation.application.dto.request.UpdateReservationRequest;
import com.bookstudio.reservation.application.dto.response.ReservationDetailResponse;
import com.bookstudio.reservation.application.dto.response.ReservationListResponse;
import com.bookstudio.shared.api.ApiError;
import com.bookstudio.shared.api.ApiSuccess;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Operations related to reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping
    @Operation(summary = "List all reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations listed successfully"),
            @ApiResponse(responseCode = "204", description = "No reservations found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiError.class), examples = @ExampleObject(name = "Internal Error", summary = "Internal server error", value = "{\"success\":false,\"status\":500,\"message\":\"Internal server error\",\"path\":\"/reservations\",\"timestamp\":\"2025-10-16T21:09:26.122Z\",\"errors\":null}")))
    })
    public ResponseEntity<ApiSuccess<List<ReservationListResponse>>> list() {
        List<ReservationListResponse> reservations = reservationService.getList();
        ApiSuccess<List<ReservationListResponse>> response = new ApiSuccess<>(
                reservations.isEmpty() ? "No reservations found" : "Reservations listed successfully",
                reservations);

        HttpStatus status = reservations.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a reservation by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format", content = @Content(schema = @Schema(implementation = ApiError.class), examples = @ExampleObject(name = "Invalid ID", summary = "Invalid ID format", value = "{\"success\":false,\"status\":400,\"message\":\"Parameter 'id' has invalid value 'abc'\",\"path\":\"/reservations/abc\",\"timestamp\":\"2025-10-16T21:09:26.122Z\",\"errors\":null}"))),
            @ApiResponse(responseCode = "404", description = "Reservation not found", content = @Content(schema = @Schema(implementation = ApiError.class), examples = @ExampleObject(name = "Not Found", summary = "Reservation not found", value = "{\"success\":false,\"status\":404,\"message\":\"Reservation not found with ID: 999\",\"path\":\"/reservations/999\",\"timestamp\":\"2025-10-16T21:09:26.122Z\",\"errors\":null}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiError.class), examples = @ExampleObject(name = "Internal Error", summary = "Internal server error", value = "{\"success\":false,\"status\":500,\"message\":\"Internal server error\",\"path\":\"/reservations/1\",\"timestamp\":\"2025-10-16T21:09:26.122Z\",\"errors\":null}")))
    })
    public ResponseEntity<ApiSuccess<ReservationDetailResponse>> get(@PathVariable Long id) {
        ReservationDetailResponse reservation = reservationService.getDetailById(id);
        return ResponseEntity.ok(new ApiSuccess<>("Reservation found", reservation));
    }

    @PostMapping
    @Operation(summary = "Create a new reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reservation created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed or malformed JSON", content = @Content(schema = @Schema(implementation = ApiError.class), examples = @ExampleObject(name = "Validation Error", summary = "Validation failed", value = "{\"success\":false,\"status\":400,\"message\":\"Validation failed\",\"path\":\"/reservations\",\"timestamp\":\"2025-10-16T21:09:26.122Z\",\"errors\":[{\"field\":\"bookId\",\"message\":\"must not be null\",\"rejectedValue\":null}]}"))),
            @ApiResponse(responseCode = "409", description = "Database constraint violation", content = @Content(schema = @Schema(implementation = ApiError.class), examples = @ExampleObject(name = "Conflict Error", summary = "Database constraint violation", value = "{\"success\":false,\"status\":409,\"message\":\"Database error: constraint violation\",\"path\":\"/reservations\",\"timestamp\":\"2025-10-16T21:09:26.122Z\",\"errors\":null}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiError.class), examples = @ExampleObject(name = "Internal Error", summary = "Internal server error", value = "{\"success\":false,\"status\":500,\"message\":\"Internal server error\",\"path\":\"/reservations\",\"timestamp\":\"2025-10-16T21:09:26.122Z\",\"errors\":null}")))
    })
    public ResponseEntity<ApiSuccess<ReservationListResponse>> create(
            @Valid @RequestBody CreateReservationRequest request) {
        ReservationListResponse created = reservationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiSuccess<>("Reservation created successfully", created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a reservation by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed, malformed JSON, or invalid ID format", content = @Content(schema = @Schema(implementation = ApiError.class), examples = @ExampleObject(name = "Validation Error", summary = "Validation failed", value = "{\"success\":false,\"status\":400,\"message\":\"Validation failed\",\"path\":\"/reservations/1\",\"timestamp\":\"2025-10-16T21:09:26.122Z\",\"errors\":[{\"field\":\"status\",\"message\":\"must not be null\",\"rejectedValue\":null}]}"))),
            @ApiResponse(responseCode = "404", description = "Reservation not found", content = @Content(schema = @Schema(implementation = ApiError.class), examples = @ExampleObject(name = "Not Found", summary = "Reservation not found", value = "{\"success\":false,\"status\":404,\"message\":\"Reservation not found\",\"path\":\"/reservations/999\",\"timestamp\":\"2025-10-16T21:09:26.122Z\",\"errors\":null}"))),
            @ApiResponse(responseCode = "409", description = "Database constraint violation", content = @Content(schema = @Schema(implementation = ApiError.class), examples = @ExampleObject(name = "Conflict Error", summary = "Database constraint violation", value = "{\"success\":false,\"status\":409,\"message\":\"Database error: constraint violation\",\"path\":\"/reservations/1\",\"timestamp\":\"2025-10-16T21:09:26.122Z\",\"errors\":null}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiError.class), examples = @ExampleObject(name = "Internal Error", summary = "Internal server error", value = "{\"success\":false,\"status\":500,\"message\":\"Internal server error\",\"path\":\"/reservations/1\",\"timestamp\":\"2025-10-16T21:09:26.122Z\",\"errors\":null}")))
    })
    public ResponseEntity<ApiSuccess<ReservationListResponse>> update(@PathVariable Long id,
            @Valid @RequestBody UpdateReservationRequest request) {
        ReservationListResponse updated = reservationService.update(id, request);
        return ResponseEntity.ok(new ApiSuccess<>("Reservation updated successfully", updated));
    }
}
