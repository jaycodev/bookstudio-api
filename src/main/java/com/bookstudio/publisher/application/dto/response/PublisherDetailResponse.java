package com.bookstudio.publisher.application.dto.response;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bookstudio.shared.domain.model.type.Status;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "nationality", "foundationYear", "website", "address", "status", "photoUrl",
        "genres" })
public record PublisherDetailResponse(
        Long id,
        String name,

        @JsonIgnore Long nationalityId,
        @JsonIgnore String nationalityCode,
        @JsonIgnore String nationalityName,

        Integer foundationYear,
        String website,
        String address,
        Status status,
        String photoUrl,
        List<GenreItem> genres) {

    public PublisherDetailResponse withGenres(List<GenreItem> genres) {
        return new PublisherDetailResponse(
                id, name, nationalityId, nationalityCode, nationalityName,
                foundationYear, website, address, status, photoUrl, genres);
    }

    @JsonGetter("nationality")
    public Map<String, Object> getNationality() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", nationalityId());
        map.put("code", nationalityCode());
        map.put("name", nationalityName());
        return map;
    }

    public record GenreItem(
            Long id,
            String name) {
    }
}
