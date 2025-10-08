package com.bookstudio.location.application.dto.request;

import java.util.List;

public record CreateLocationRequest(
    String name,
    String description,
    List<CreateShelfRequest> shelves
) {}
