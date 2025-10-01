package com.bookstudio.payment.presentation;

import com.bookstudio.payment.application.PaymentService;
import com.bookstudio.payment.application.dto.request.CreatePaymentRequest;
import com.bookstudio.payment.application.dto.request.UpdatePaymentRequest;
import com.bookstudio.payment.application.dto.response.PaymentDetailResponse;
import com.bookstudio.payment.application.dto.response.PaymentListResponse;
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
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Operations related to payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    @Operation(summary = "List all payments")
    public ResponseEntity<?> list() {
        List<PaymentListResponse> payments = paymentService.getList();
        if (payments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiErrorResponse(false, "No payments found.", "no_content", 204));
        }
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a payment by ID")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            PaymentDetailResponse payment = paymentService.getDetailById(id);
            return ResponseEntity.ok(payment);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, "Payment not found.", "not_found", 404));
        }
    }

    @PostMapping
    @Operation(summary = "Create a new payment")
    public ResponseEntity<?> create(@RequestBody CreatePaymentRequest request) {
        try {
            PaymentListResponse created = paymentService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponse(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a payment by ID")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdatePaymentRequest request) {
        try {
            PaymentListResponse updated = paymentService.update(id, request);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, e.getMessage(), "update_failed", 404));
        }
    }
}
