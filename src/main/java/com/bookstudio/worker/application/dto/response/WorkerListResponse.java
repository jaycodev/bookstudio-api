package com.bookstudio.worker.application.dto.response;

import com.bookstudio.worker.domain.model.type.WorkerStatus;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonPropertyOrder({ "id", "profilePhotoUrl", "username", "email", "fullName", "role", "status" })
public record WorkerListResponse(
        Long id,
        String profilePhotoUrl,
        String username,
        String email,
        String fullName,

        @JsonIgnore Long roleId,
        @JsonIgnore String roleName,

        WorkerStatus status) {

    @JsonGetter("role")
    public Map<String, Object> getRole() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", roleId());
        map.put("name", roleName());
        return map;
    }
}
