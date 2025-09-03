package com.bookstudio.payment.service;

import com.bookstudio.fine.model.Fine;
import com.bookstudio.fine.model.FineStatus;
import com.bookstudio.fine.repository.FineRepository;
import com.bookstudio.fine.service.FineService;
import com.bookstudio.payment.dto.CreatePaymentDto;
import com.bookstudio.payment.dto.PaymentDetailDto;
import com.bookstudio.payment.dto.PaymentListDto;
import com.bookstudio.payment.dto.PaymentSummaryDto;
import com.bookstudio.payment.dto.UpdatePaymentDto;
import com.bookstudio.payment.model.Payment;
import com.bookstudio.payment.relation.PaymentFine;
import com.bookstudio.payment.relation.PaymentFineId;
import com.bookstudio.payment.repository.PaymentFineRepository;
import com.bookstudio.payment.repository.PaymentRepository;
import com.bookstudio.reader.service.ReaderService;

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
public class PaymentService {

    @PersistenceContext
    private EntityManager entityManager;

    private final PaymentRepository paymentRepository;
    private final FineRepository fineRepository;
    private final PaymentFineRepository paymentFineRepository;

    private final ReaderService readerService;
    private final FineService fineService;

    public List<PaymentListDto> getList() {
        return paymentRepository.findList();
    }

    public Optional<Payment> findById(Long paymentId) {
        return paymentRepository.findById(paymentId);
    }

    public PaymentDetailDto getInfoById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with ID: " + paymentId));

        return PaymentDetailDto.builder()
                .id(payment.getPaymentId())
                .code(payment.getCode())
                .reader(readerService.toSummaryDto(payment.getReader()))
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .method(payment.getMethod().name())
                .fines(paymentFineRepository.findFineSummariesByPaymentId(payment.getPaymentId()))
                .build();
    }

    @Transactional
    public PaymentListDto create(CreatePaymentDto dto) {
        Payment payment = Payment.builder()
                .reader(readerService.findById(dto.getReaderId())
                        .orElseThrow(() -> new RuntimeException("Reader not found with ID: " + dto.getReaderId())))
                .amount(dto.getAmount())
                .paymentDate(dto.getPaymentDate())
                .method(dto.getMethod())
                .build();

        Payment saved = paymentRepository.save(payment);
        entityManager.refresh(saved);

        if (dto.getFineIds() != null) {
            for (Long fineId : dto.getFineIds()) {
                Fine fine = fineService.findById(fineId)
                        .orElseThrow(() -> new RuntimeException("Fine not found with ID: " + fineId));

                PaymentFine relation = PaymentFine.builder()
                        .id(new PaymentFineId(saved.getPaymentId(), fine.getFineId()))
                        .payment(saved)
                        .fine(fine)
                        .build();

                paymentFineRepository.save(relation);

                fine.setStatus(FineStatus.PAGADO);
                fineRepository.save(fine);
            }
        }

        return toListDto(saved);
    }

    @Transactional
    public PaymentListDto update(Long paymentId, UpdatePaymentDto dto) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + paymentId));

        payment.setReader(readerService.findById(dto.getReaderId())
                .orElseThrow(() -> new RuntimeException("Reader not found with ID: " + dto.getReaderId())));
        payment.setAmount(dto.getAmount());
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setMethod(dto.getMethod());

        Payment saved = paymentRepository.save(payment);

        paymentFineRepository.deleteAllByPayment(saved);

        if (dto.getFineIds() != null) {
            for (Long fineId : dto.getFineIds()) {
                Fine fine = fineService.findById(fineId)
                        .orElseThrow(() -> new RuntimeException("Fine not found with ID: " + fineId));

                PaymentFine relation = PaymentFine.builder()
                        .id(new PaymentFineId(saved.getPaymentId(), fine.getFineId()))
                        .payment(saved)
                        .fine(fine)
                        .build();

                paymentFineRepository.save(relation);

                fine.setStatus(FineStatus.PAGADO);
                fineRepository.save(fine);
            }
        }

        return toListDto(saved);
    }

    public PaymentSummaryDto toSummary(Payment payment) {
        return PaymentSummaryDto.builder()
                .id(payment.getPaymentId())
                .code(payment.getCode())
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .method(payment.getMethod().name())
                .build();
    }

    private PaymentListDto toListDto(Payment payment) {
        return new PaymentListDto(
                payment.getPaymentId(),
                payment.getCode(),
                paymentFineRepository.countByPayment(payment),
                payment.getReader().getCode(),
                payment.getReader().getFullName(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getMethod());
    }
}
