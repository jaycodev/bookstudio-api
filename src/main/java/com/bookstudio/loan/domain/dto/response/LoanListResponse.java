package com.bookstudio.loan.domain.dto.response;

import java.time.LocalDate;
import java.util.Map;

public record LoanListResponse(
        Long id,
        String code,
        Reader reader,
        LocalDate loanDate,
        Long itemCount,
        Map<String, Long> statusCounts) {

    public LoanListResponse(
            Long id,
            String code,
            Reader reader,
            LocalDate loanDate,
            ItemCounts counts) {
        this(
                id,
                code,
                reader,
                loanDate,
                counts.total(),
                Map.of(
                        "borrowed", counts.borrowed(),
                        "returned", counts.returned(),
                        "overdue", counts.overdue(),
                        "lost", counts.lost(),
                        "canceled", counts.canceled()));
    }

    public record Reader(
            Long id,
            String code,
            String fullName) {
    }

    public record ItemCounts(
            Long borrowed,
            Long returned,
            Long overdue,
            Long lost,
            Long canceled) {
        public Long total() {
            return borrowed + returned + overdue + lost + canceled;
        }
    }
}
