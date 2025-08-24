package com.bookstudio.publisher.dto;

import com.bookstudio.shared.enums.Status;

public record PublisherListDto(
    Long id,
    String photoUrl,
    String name,
    String nationalityName,
    String website,
    Status status
) {}