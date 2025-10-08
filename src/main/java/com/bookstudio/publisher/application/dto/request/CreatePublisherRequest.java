package com.bookstudio.publisher.application.dto.request;

import java.util.List;

import com.bookstudio.shared.domain.model.type.Status;

public record CreatePublisherRequest(
    String name,
    Long nationalityId,
    Integer foundationYear,
    String website,
    String address,
    Status status,
    String photoUrl,
    List<Long> genreIds
) {}
