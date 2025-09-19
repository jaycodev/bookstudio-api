package com.bookstudio.reservation.application.dto.response;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bookstudio.copy.domain.model.type.CopyStatus;
import com.bookstudio.reservation.domain.model.type.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "code", "reader", "copy", "reservationDate", "status" })
public record ReservationDetailResponse(
        Long id,
        String code,

        @JsonIgnore Long readerId,
        @JsonIgnore String readerCode,
        @JsonIgnore String readerFullName,

        @JsonIgnore Long copyId,
        @JsonIgnore String copyCode,
        @JsonIgnore String copyBarcode,
        @JsonIgnore CopyStatus copyStatus,

        LocalDate reservationDate,
        ReservationStatus status) {

    @JsonGetter("reader")
    public Map<String, Object> getReader() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", readerId());
        map.put("code", readerCode());
        map.put("fullName", readerFullName());
        return map;
    }

    @JsonGetter("copy")
    public Map<String, Object> getCopy() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", copyId());
        map.put("code", copyCode());
        map.put("barcode", copyBarcode());
        map.put("status", copyStatus());
        return map;
    }
}
