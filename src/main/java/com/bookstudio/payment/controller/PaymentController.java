package com.bookstudio.payment.controller;

import com.bookstudio.payment.dto.CreatePaymentDto;
import com.bookstudio.payment.dto.PaymentDetailDto;
import com.bookstudio.payment.dto.PaymentListDto;
import com.bookstudio.payment.dto.UpdatePaymentDto;
import com.bookstudio.payment.service.PaymentService;
import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<?> list() {
        List<PaymentListDto> payments = paymentService.getList();
        if (payments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiError(false, "No payments found.", "no_content", 204));
        }
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            PaymentDetailDto payment = paymentService.getInfoById(id);
            return ResponseEntity.ok(payment);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, "Payment not found.", "not_found", 404));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreatePaymentDto dto) {
        try {
            PaymentListDto created = paymentService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiError(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdatePaymentDto dto) {
        try {
            PaymentListDto updated = paymentService.update(dto);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, e.getMessage(), "update_failed", 404));
        }
    }
}
