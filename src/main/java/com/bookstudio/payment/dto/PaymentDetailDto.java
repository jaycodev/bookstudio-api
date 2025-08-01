package com.bookstudio.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.bookstudio.fine.dto.FineSummaryDto;
import com.bookstudio.reader.dto.ReaderSummaryDto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetailDto {
    private Long id;
    private String code;
    private ReaderSummaryDto reader;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String method;
    private List<FineSummaryDto> fines;
}
