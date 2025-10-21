package com.bookstudio.payment.application;

import com.bookstudio.fine.domain.model.Fine;
import com.bookstudio.fine.domain.model.type.FineStatus;
import com.bookstudio.fine.infrastructure.repository.FineRepository;
import com.bookstudio.payment.application.dto.request.CreatePaymentRequest;
import com.bookstudio.payment.application.dto.request.UpdatePaymentRequest;
import com.bookstudio.payment.application.dto.response.PaymentDetailResponse;
import com.bookstudio.payment.application.dto.response.PaymentFilterOptionsResponse;
import com.bookstudio.payment.application.dto.response.PaymentListResponse;
import com.bookstudio.payment.domain.model.Payment;
import com.bookstudio.payment.domain.model.PaymentFine;
import com.bookstudio.payment.domain.model.PaymentFineId;
import com.bookstudio.payment.infrastructure.repository.PaymentFineRepository;
import com.bookstudio.payment.infrastructure.repository.PaymentRepository;
import com.bookstudio.reader.infrastructure.repository.ReaderRepository;
import com.bookstudio.shared.exception.ResourceNotFoundException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
    @PersistenceContext
    private EntityManager entityManager;

    private final PaymentRepository paymentRepository;
    private final FineRepository fineRepository;
    private final PaymentFineRepository paymentFineRepository;
    private final ReaderRepository readerRepository;

    public List<PaymentListResponse> getList() {
        return paymentRepository.findList();
    }

    public PaymentFilterOptionsResponse getFilterOptions() {
        return new PaymentFilterOptionsResponse(
                readerRepository.findForOptions());
    }

    public PaymentDetailResponse getDetailById(Long id) {
        PaymentDetailResponse base = paymentRepository.findDetailById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));

        return base.withFines(paymentFineRepository.findFinesItemsByPaymentId(id));
    }

    @Transactional
    public PaymentListResponse create(CreatePaymentRequest request) {
        Payment payment = new Payment();
        payment.setReader(readerRepository.findById(request.readerId())
                .orElseThrow(() -> new ResourceNotFoundException("Reader not found with ID: " + request.readerId())));

        payment.setAmount(request.amount());
        payment.setPaymentDate(request.paymentDate());
        payment.setMethod(request.method());

        Payment saved = paymentRepository.save(payment);
        entityManager.refresh(saved);

        if (request.fineIds() != null) {
            for (Long fineId : request.fineIds()) {
                Fine fine = fineRepository.findById(fineId)
                        .orElseThrow(() -> new ResourceNotFoundException("Fine not found with ID: " + fineId));

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
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));

        payment.setReader(readerRepository.findById(request.readerId())
                .orElseThrow(() -> new ResourceNotFoundException("Reader not found with ID: " + request.readerId())));

        payment.setAmount(request.amount());
        payment.setPaymentDate(request.paymentDate());
        payment.setMethod(request.method());

        Payment updated = paymentRepository.save(payment);

        paymentFineRepository.deleteAllByPayment(updated);

        if (request.fineIds() != null) {
            for (Long fineId : request.fineIds()) {
                Fine fine = fineRepository.findById(fineId)
                        .orElseThrow(() -> new ResourceNotFoundException("Fine not found with ID: " + fineId));

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

                payment.getReader().getId(),
                payment.getReader().getCode(),
                payment.getReader().getFullName(),

                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getMethod());
    }
}
