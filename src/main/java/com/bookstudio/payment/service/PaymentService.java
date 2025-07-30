package com.bookstudio.payment.service;

import com.bookstudio.payment.dto.CreatePaymentDto;
import com.bookstudio.payment.dto.PaymentResponseDto;
import com.bookstudio.payment.dto.UpdatePaymentDto;
import com.bookstudio.payment.model.Payment;
import com.bookstudio.payment.projection.PaymentInfoProjection;
import com.bookstudio.payment.projection.PaymentListProjection;
import com.bookstudio.payment.repository.PaymentRepository;
import com.bookstudio.reader.service.ReaderService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    
    private final ReaderService readerService;

    public List<PaymentListProjection> getList() {
        return paymentRepository.findList();
    }

    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    public Optional<PaymentInfoProjection> getInfoById(Long id) {
        return paymentRepository.findInfoById(id);
    }

    @Transactional
    public PaymentResponseDto create(CreatePaymentDto dto) {
        Payment payment = Payment.builder()
                .reader(readerService.findById(dto.getReaderId())
                        .orElseThrow(() -> new RuntimeException("Reader not found with ID: " + dto.getReaderId())))
                .amount(dto.getAmount())
                .paymentDate(dto.getPaymentDate())
                .method(dto.getMethod())
                .build();

        Payment saved = paymentRepository.save(payment);

        return new PaymentResponseDto(
                saved.getPaymentId(),
                saved.getCode(),
                saved.getReader().getFirstName() + " " + saved.getReader().getLastName(),
                saved.getAmount(),
                saved.getPaymentDate(),
                saved.getMethod().name()
        );
    }

    @Transactional
    public PaymentResponseDto update(UpdatePaymentDto dto) {
        Payment payment = paymentRepository.findById(dto.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + dto.getPaymentId()));

        payment.setReader(readerService.findById(dto.getReaderId())
                .orElseThrow(() -> new RuntimeException("Reader not found with ID: " + dto.getReaderId())));
        payment.setAmount(dto.getAmount());
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setMethod(dto.getMethod());

        Payment saved = paymentRepository.save(payment);

        return new PaymentResponseDto(
                saved.getPaymentId(),
                saved.getCode(),
                saved.getReader().getFirstName() + " " + saved.getReader().getLastName(),
                saved.getAmount(),
                saved.getPaymentDate(),
                saved.getMethod().name()
        );
    }
}
