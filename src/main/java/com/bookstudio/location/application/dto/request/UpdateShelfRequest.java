package com.bookstudio.location.application.dto.request;

public record UpdateShelfRequest(
    Long id,
    String code,
    String floor,
    String description
) {}
