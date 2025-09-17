package com.bookstudio.author.application.dto.response;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bookstudio.shared.domain.model.type.Status;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "photoUrl", "name", "nationality", "birthDate", "status" })
public record AuthorListResponse(
        Long id,
        String photoUrl,
        String name,

        @JsonIgnore Long nationalityId,
        @JsonIgnore String nationalityCode,
        @JsonIgnore String nationalityName,

        LocalDate birthDate,
        Status status) {

    @JsonGetter("nationality")
    public Map<String, Object> getNationality() {
        Map<String, Object> nationality = new LinkedHashMap<>();
        nationality.put("id", nationalityId);
        nationality.put("code", nationalityCode);
        nationality.put("name", nationalityName);
        return nationality;
    }
}
