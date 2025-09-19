package com.bookstudio.worker.application.dto.response;

import com.bookstudio.worker.domain.model.type.WorkerStatus;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonPropertyOrder({ "id", "username", "email", "firstName", "lastName", "role", "profilePhotoUrl", "status" })
public record WorkerDetailResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,

        @JsonIgnore Long roleId,
        @JsonIgnore String roleName,

        String profilePhotoUrl,
        WorkerStatus status) {

    @JsonGetter("role")
    public Map<String, Object> getRole() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", roleId());
        map.put("name", roleName());
        return map;
    }
}
