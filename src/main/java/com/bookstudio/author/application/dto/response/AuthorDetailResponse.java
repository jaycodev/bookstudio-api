package com.bookstudio.author.application.dto.response;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bookstudio.shared.domain.model.type.Status;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "nationality", "birthDate", "biography", "status", "photoUrl" })
public record AuthorDetailResponse(
        Long id,
        String name,

        @JsonIgnore Long nationalityId,
        @JsonIgnore String nationalityCode,
        @JsonIgnore String nationalityName,

        LocalDate birthDate,
        String biography,
        Status status,
        String photoUrl) {

    @JsonGetter("nationality")
    public Map<String, Object> getNationality() {
        Map<String, Object> nationality = new LinkedHashMap<>();
        nationality.put("id", nationalityId);
        nationality.put("code", nationalityCode);
        nationality.put("name", nationalityName);
        return nationality;
    }
}
