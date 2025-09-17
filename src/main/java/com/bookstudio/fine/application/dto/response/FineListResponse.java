package com.bookstudio.fine.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bookstudio.fine.domain.model.type.FineStatus;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "code", "loan", "copy", "amount", "daysLate", "issuedAt", "status" })
public record FineListResponse(
        Long id,
        String code,

        @JsonIgnore Long loanId,
        @JsonIgnore String loanCode,

        @JsonIgnore Long copyId,
        @JsonIgnore String copyCode,

        BigDecimal amount,
        Integer daysLate,
        LocalDate issuedAt,
        FineStatus status) {

    @JsonGetter("loan")
    public Map<String, Object> getLoan() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", loanId());
        map.put("code", loanCode());
        return map;
    }

    @JsonGetter("copy")
    public Map<String, Object> getCopy() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", copyId());
        map.put("code", copyCode());
        return map;
    }
}
