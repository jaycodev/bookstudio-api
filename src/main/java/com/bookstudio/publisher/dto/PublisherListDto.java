package com.bookstudio.publisher.dto;

import com.bookstudio.shared.enums.Status;

public record PublisherListDto(
    String photoUrl,
    String name,
    String nationalityName,
    String website,
    Status status,
    Long id
) {}