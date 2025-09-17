package com.bookstudio.payment.application;

import com.bookstudio.fine.application.FineService;
import com.bookstudio.fine.domain.model.Fine;
import com.bookstudio.fine.domain.model.type.FineStatus;
import com.bookstudio.fine.infrastructure.repository.FineRepository;
import com.bookstudio.payment.domain.dto.request.CreatePaymentRequest;
import com.bookstudio.payment.domain.dto.request.UpdatePaymentRequest;
import com.bookstudio.payment.domain.dto.response.PaymentDetailResponse;
import com.bookstudio.payment.domain.dto.response.PaymentListResponse;
import com.bookstudio.payment.domain.model.Payment;
import com.bookstudio.payment.domain.model.PaymentFine;
import com.bookstudio.payment.domain.model.PaymentFineId;
import com.bookstudio.payment.infrastructure.repository.PaymentFineRepository;
import com.bookstudio.payment.infrastructure.repository.PaymentRepository;
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
public class PaymentService {

    @PersistenceContext
    private EntityManager entityManager;

    private final PaymentRepository paymentRepository;
    private final FineRepository fineRepository;
    private final PaymentFineRepository paymentFineRepository;

    private final ReaderService readerService;
    private final FineService fineService;

    public List<PaymentListResponse> getList() {
        return paymentRepository.findList();
    }

    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    public PaymentDetailResponse getDetailById(Long id) {
        PaymentDetailResponse base = paymentRepository.findDetailById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with ID: " + id));

        return base.withFines(paymentFineRepository.findFinesItemsByPaymentId(id));
    }

    @Transactional
    public PaymentListResponse create(CreatePaymentRequest request) {
        Payment payment = Payment.builder()
                .reader(readerService.findById(request.getReaderId())
                        .orElseThrow(() -> new RuntimeException("Reader not found with ID: " + request.getReaderId())))
                .amount(request.getAmount())
                .paymentDate(request.getPaymentDate())
                .method(request.getMethod())
                .build();

        Payment saved = paymentRepository.save(payment);
        entityManager.refresh(saved);

        if (request.getFineIds() != null) {
            for (Long fineId : request.getFineIds()) {
                Fine fine = fineService.findById(fineId)
                        .orElseThrow(() -> new RuntimeException("Fine not found with ID: " + fineId));

                PaymentFine relation = PaymentFine.builder()
                        .id(new PaymentFineId(saved.getId(), fine.getId()))
                        .payment(saved)
                        .fine(fine)
                        .build();

                paymentFineRepository.save(relation);

                fine.setStatus(FineStatus.PAGADO);
                fineRepository.save(fine);
            }
        }

        return toListResponse(saved);
    }

    @Transactional
    public PaymentListResponse update(Long id, UpdatePaymentRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + id));

        payment.setReader(readerService.findById(request.getReaderId())
                .orElseThrow(() -> new RuntimeException("Reader not found with ID: " + request.getReaderId())));
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(request.getPaymentDate());
        payment.setMethod(request.getMethod());

        Payment updated = paymentRepository.save(payment);

        paymentFineRepository.deleteAllByPayment(updated);

        if (request.getFineIds() != null) {
            for (Long fineId : request.getFineIds()) {
                Fine fine = fineService.findById(fineId)
                        .orElseThrow(() -> new RuntimeException("Fine not found with ID: " + fineId));

                PaymentFine relation = PaymentFine.builder()
                        .id(new PaymentFineId(updated.getId(), fine.getId()))
                        .payment(updated)
                        .fine(fine)
                        .build();

                paymentFineRepository.save(relation);

                fine.setStatus(FineStatus.PAGADO);
                fineRepository.save(fine);
            }
        }

        return toListResponse(updated);
    }

    private PaymentListResponse toListResponse(Payment payment) {
        return new PaymentListResponse(
                payment.getId(),
                payment.getCode(),
                paymentFineRepository.countByPayment(payment),
                new PaymentListResponse.Reader(
                        payment.getReader().getId(),
                        payment.getReader().getCode(),
                        payment.getReader().getFullName()),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getMethod());
    }
}
