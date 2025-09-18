package com.bookstudio.publisher.application.dto.response;

import com.bookstudio.shared.domain.model.type.Status;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonPropertyOrder({ "id", "photoUrl", "name", "nationality", "website", "address", "status" })
public record PublisherListResponse(
        Long id,
        String photoUrl,
        String name,

        @JsonIgnore Long nationalityId,
        @JsonIgnore String nationalityCode,
        @JsonIgnore String nationalityName,

        String website,
        String address,
        Status status) {

    @JsonGetter("nationality")
    public Map<String, Object> getNationality() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", nationalityId());
        map.put("code", nationalityCode());
        map.put("name", nationalityName());
        return map;
    }
}
