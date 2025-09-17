package com.bookstudio.fine.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bookstudio.copy.domain.model.type.CopyStatus;
import com.bookstudio.fine.domain.model.type.FineStatus;
import com.bookstudio.loan.domain.model.type.LoanItemStatus;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "code", "loanItem", "amount", "daysLate", "status", "issuedAt" })
public record FineDetailResponse(
        Long id,
        String code,

        @JsonIgnore Long loanItemCopyId,
        @JsonIgnore String loanItemCopyCode,
        @JsonIgnore String loanItemCopyBarcode,
        @JsonIgnore CopyStatus loanItemCopyStatus,
        
        @JsonIgnore LocalDate loanItemDueDate,
        @JsonIgnore LocalDate loanItemReturnDate,
        @JsonIgnore LoanItemStatus loanItemStatus,

        BigDecimal amount,
        Integer daysLate,
        FineStatus status,
        LocalDate issuedAt) {

    @JsonGetter("loanItem")
    public Map<String, Object> getLoanItem() {
        Map<String, Object> map = new LinkedHashMap<>();

        Map<String, Object> copyMap = new LinkedHashMap<>();
        copyMap.put("id", loanItemCopyId());
        copyMap.put("code", loanItemCopyCode());
        copyMap.put("barcode", loanItemCopyBarcode());
        copyMap.put("status", loanItemCopyStatus());

        map.put("copy", copyMap);
        map.put("dueDate", loanItemDueDate());
        map.put("returnDate", loanItemReturnDate());
        map.put("status", loanItemStatus());

        return map;
    }
}
