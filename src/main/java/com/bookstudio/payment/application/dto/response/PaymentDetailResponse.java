package com.bookstudio.payment.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bookstudio.fine.domain.model.type.FineStatus;
import com.bookstudio.payment.domain.model.type.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "code", "reader", "amount", "paymentDate", "method", "fines" })
public record PaymentDetailResponse(
        Long id,
        String code,

        @JsonIgnore Long readerId,
        @JsonIgnore String readerCode,
        @JsonIgnore String readerFullName,

        BigDecimal amount,
        LocalDate paymentDate,
        PaymentMethod method,
        List<FineItem> fines) {

    public PaymentDetailResponse withFines(List<FineItem> fines) {
        return new PaymentDetailResponse(
                id, code, readerId, readerCode, readerFullName, amount, paymentDate, method, fines);
    }

    @JsonGetter("reader")
    public Map<String, Object> getReader() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", readerId());
        map.put("code", readerCode());
        map.put("fullName", readerFullName());
        return map;
    }

    public record FineItem(
            Long id,
            String code,
            BigDecimal amount,
            FineStatus status) {
    }
}
