package com.bookstudio.location.application.dto.request;

public record CreateShelfRequest(
    String code,
    String floor,
    String description
) {}
