package com.bookstudio.loan.mapper;

import com.bookstudio.loan.dto.LoanListDto;
import com.bookstudio.loan.dto.LoanListRawDto;

import java.util.Map;

public class LoanMapper {

    public static LoanListDto fromRaw(LoanListRawDto raw) {
        int itemCount = (int) (raw.borrowedCount()
                + raw.returnedCount()
                + raw.overdueCount()
                + raw.lostCount()
                + raw.canceledCount());

        Map<String, Long> statusCounts = Map.of(
                "borrowed", raw.borrowedCount(),
                "returned", raw.returnedCount(),
                "overdue", raw.overdueCount(),
                "lost", raw.lostCount(),
                "canceled", raw.canceledCount());

        return new LoanListDto(
                raw.id(),
                raw.code(),
                raw.readerCode(),
                raw.readerFullName(),
                raw.loanDate(),
                itemCount,
                statusCounts);
    }
}
