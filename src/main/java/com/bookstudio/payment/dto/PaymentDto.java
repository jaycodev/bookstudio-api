package com.bookstudio.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.bookstudio.fine.dto.FineDto;
import com.bookstudio.reader.dto.ReaderDto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private Long id;
    private String code;
    private ReaderDto reader;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String method;
    private List<FineDto> fines;
}
