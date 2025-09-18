package com.bookstudio.loan.application.dto.response;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "code", "reader", "loanDate", "itemCount", "statusCounts" })
public record LoanListResponse(
        Long id,
        String code,

        @JsonIgnore Long readerId,
        @JsonIgnore String readerCode,
        @JsonIgnore String readerFullName,

        LocalDate loanDate,
        Long itemCount,

        @JsonIgnore Map<String, Long> statusCounts) {

    public LoanListResponse(
            Long id,
            String code,

            Long readerId,
            String readerCode,
            String readerFullName,

            LocalDate loanDate,

            Long borrowedCount,
            Long returnedCount,
            Long overdueCount,
            Long lostCount,
            Long canceledCount) {

        this(
                id,
                code,
                readerId,
                readerCode,
                readerFullName,
                loanDate,
                borrowedCount + returnedCount + overdueCount + lostCount + canceledCount,
                buildStatusCounts(borrowedCount, returnedCount, overdueCount, lostCount, canceledCount));
    }

    private static Map<String, Long> buildStatusCounts(
            Long borrowed, Long returned, Long overdue, Long lost, Long canceled) {
        Map<String, Long> map = new LinkedHashMap<>();
        map.put("borrowed", borrowed);
        map.put("returned", returned);
        map.put("overdue", overdue);
        map.put("lost", lost);
        map.put("canceled", canceled);
        return map;
    }

    @JsonGetter("reader")
    public Map<String, Object> getReader() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", readerId());
        map.put("code", readerCode());
        map.put("fullName", readerFullName());
        return map;
    }

    @JsonGetter("statusCounts")
    public Map<String, Long> getStatusCounts() {
        return statusCounts();
    }
}
