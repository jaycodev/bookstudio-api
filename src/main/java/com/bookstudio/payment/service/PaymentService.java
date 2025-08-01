package com.bookstudio.payment.service;

import com.bookstudio.copy.dto.CopyDto;
import com.bookstudio.fine.dto.FineDto;
import com.bookstudio.fine.model.Fine;
import com.bookstudio.fine.repository.FineRepository;
import com.bookstudio.fine.service.FineService;
import com.bookstudio.loan.dto.LoanItemDto;
import com.bookstudio.payment.dto.CreatePaymentDto;
import com.bookstudio.payment.dto.PaymentDto;
import com.bookstudio.payment.dto.PaymentListDto;
import com.bookstudio.payment.dto.UpdatePaymentDto;
import com.bookstudio.payment.model.Payment;
import com.bookstudio.payment.relation.PaymentFine;
import com.bookstudio.payment.relation.PaymentFineId;
import com.bookstudio.payment.repository.PaymentFineRepository;
import com.bookstudio.payment.repository.PaymentRepository;
import com.bookstudio.reader.service.ReaderService;
import com.bookstudio.shared.enums.FineStatus;

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

    public PaymentDto getInfoById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with ID: " + paymentId));
        return toDto(payment);
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

                fine.setStatus(FineStatus.pagado);
                fineRepository.save(fine);
            }
        }

        return toListDto(saved);
    }

    @Transactional
    public PaymentListDto update(UpdatePaymentDto dto) {
        Payment payment = paymentRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + dto.getId()));

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

                fine.setStatus(FineStatus.pagado);
                fineRepository.save(fine);
            }
        }

        return toListDto(saved);
    }

    public PaymentDto toDto(Payment payment) {
        List<FineDto> fines = paymentFineRepository.findFineFlatDtosByPaymentId(payment.getPaymentId()).stream()
                .map(flat -> FineDto.builder()
                        .id(flat.id())
                        .code(flat.code())
                        .loanItem(LoanItemDto.builder()
                                .copy(CopyDto.builder()
                                        .code(flat.copyCode())
                                        .build())
                                .dueDate(flat.dueDate())
                                .returnDate(flat.returnDate())
                                .status(flat.loanItemStatus().name())
                                .build())
                        .amount(flat.amount())
                        .daysLate(flat.daysLate())
                        .status(flat.status().name())
                        .issuedAt(flat.issuedAt())
                        .build())
                .toList();

        return PaymentDto.builder()
                .id(payment.getPaymentId())
                .code(payment.getCode())
                .reader(readerService.toDto(payment.getReader()))
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .method(payment.getMethod().name())
                .fines(fines)
                .build();
    }

    private PaymentListDto toListDto(Payment payment) {
        return new PaymentListDto(
                payment.getCode(),
                payment.getReader().getFullName(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getMethod(),
                payment.getPaymentId());
    }
}
