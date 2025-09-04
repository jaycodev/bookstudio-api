package com.bookstudio.publisher.dto;

import com.bookstudio.shared.enums.Status;

public record PublisherListDto(
    Long id,
    String photoUrl,
    String name,
    Long nationalityId,
    String nationalityCode,
    String nationalityName,
    String website,
    String address,
    Status status
) {}